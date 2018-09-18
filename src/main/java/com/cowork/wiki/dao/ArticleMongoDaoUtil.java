package com.cowork.wiki.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cowork.wiki.model.Article;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.yan.common.mongodb.MongoDBConfig;
import com.yan.common.util.SchemaDocumentUtil;

@Repository
public class ArticleMongoDaoUtil {

	@Autowired
	private MongoDBConfig dataSource;
	
	public MongoDBConfig getDataSource() {
		return dataSource;
	}

	public void setDataSource(MongoDBConfig dataSource) {
		this.dataSource = dataSource;
	}
	
	public Integer findMaxArticleId() {
		Integer maxArticleId = 0;
		
		//To connect to a single MongoDB instance:
		//You can explicitly specify the hostname and the port:
		MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
		                                         Arrays.asList(credential));
		//Access a Database
		MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
		
		//Access a Collection
		MongoCollection<Document> collection = database.getCollection("Article");
		
		//计算articleId的最大值
		AggregateIterable<Document> iterable = collection.aggregate(Arrays.asList(
		            new Document("$group", new Document("_id", "max").append("max_value", new Document("$max", "$articleId")))
		    ));
		
		if(iterable != null){
			for(Document doc : iterable){
				maxArticleId = doc.getInteger("max_value");
			}
		}
		mongoClient.close();
		return maxArticleId;
	}
	
	public String insertArticle(Article article){

		//To connect to a single MongoDB instance:
		//You can explicitly specify the hostname and the port:
		MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
		                                         Arrays.asList(credential));
		//Access a Database
		MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
		
		//Access a Collection
		MongoCollection<Document> collection = database.getCollection("Article");
		
		//Create a Document
		Document doc = (Document)SchemaDocumentUtil.schameToDocument(article, Article.class);;
		
		//Insert a Document
		collection.insertOne(doc);
		 
		String id = null;
		if(doc.get("_id") != null){
			id = doc.get("_id").toString();
		}
		mongoClient.close();
		return id;
	}

	public List<Article> findArticleDocumentsByCondition(Map<String, Object> condition){
		List<Article> articles = null;
		
		if(condition != null && condition.size() > 0) {
			
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
			                                         Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			List<Bson> bsons = new ArrayList<Bson>(0);
			
			//分页的页码
			int page = 1;
			//分页每页条数
			int rows = 10;
			
			for(Iterator<Entry<String, Object>> iterator = condition.entrySet().iterator();iterator.hasNext();) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				
				//因为查询条件改为了and，所以当条件为空字符串的时候不向查询条件中拼写
				if(value != null && !"".equals(value.toString().trim())){
					if("id".equals(key)) {
						bsons.add(Filters.eq("_id", new ObjectId(value.toString())));
					}else if ("articleId".equals(key)) {
						bsons.add(Filters.eq("articleId", value));
					}else if ("insertTimeStart".equals(key)) {
						bsons.add(Filters.gte("insertTime", value));
					}else if ("insertTimeEnd".equals(key)) {
						bsons.add(Filters.lte("insertTime", value));
					}else if ("page".equals(key)) {
						page = Integer.parseInt(value.toString());
					}else if ("rows".equals(key)) {
						rows = Integer.parseInt(value.toString());
					}else {
						//其他的进行右模糊查询
						//参考下regex的使用
						//i  　如果设置了这个修饰符，模式中的字母会进行大小写不敏感匹配。
						//m   默认情况下，PCRE 认为目标字符串是由单行字符组成的(然而实际上它可能会包含多行).如果目标字符串 中没有 "\n"字符，或者模式中没有出现“行首”/“行末”字符，设置这个修饰符不产生任何影响。
						//s    如果设置了这个修饰符，模式中的点号元字符匹配所有字符，包含换行符。如果没有这个修饰符，点号不匹配换行符。
						//x    如果设置了这个修饰符，模式中的没有经过转义的或不在字符类中的空白数据字符总会被忽略，并且位于一个未转义的字符类外部的#字符和下一个换行符之间的字符也被忽略。 这个修饰符使被编译模式中可以包含注释。 注意：这仅用于数据字符。 空白字符 还是不能在模式的特殊字符序列中出现，比如序列 。
						//注：JavaScript只提供了i和m选项，x和s选项必须使用$regex操作符
						
						//在命令行的时候pattern左右使用//包起来，是因为通过//来表示包起来的是pattern，但是如果java中再将//拼接到字符串中，那么//就会当做pattern的一部分去匹配，就会出现问题
						//debug的时候，发现pattern中居然包括//这是不对的
						bsons.add(Filters.regex(key, "" + value.toString() + ".*", "i"));
					}
				}
				
			}
			
			int limit = rows;
			int skip = 0;
			if(page >= 0){
				skip = (page - 1) * rows;
			}
			
			//如果要在find中传入bson数组，那么bson数组必须不能为空
			List<Document> docs = null;
			if(bsons != null && bsons.size() > 0){
				docs = collection.find(Filters.or(bsons)).limit(limit).skip(skip).sort(new Document("updateTime", -1)).into(new ArrayList<Document>());
			}else{
				docs = collection.find().limit(limit).skip(skip).sort(new Document("updateTime", -1)).into(new ArrayList<Document>());
			}
			
			if(docs != null){
				articles = new ArrayList<Article>();
				
				for(Document doc : docs){
					Article article = new Article();
					//将document转换为article
					//System.out.println(doc.get("_id"));
					article = (Article)SchemaDocumentUtil.documentToSchame(doc, Article.class);
					
					articles.add(article);
				}
			}
			mongoClient.close();
		}
		return articles;
	}
	
	public Long countArticleDocumentsByCondition(Map<String, Object> condition){
		long count = 0L;
		
		if(condition != null && condition.size() > 0) {
			
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
			                                         Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			List<Bson> bsons = new ArrayList<Bson>(0);
			
			for(Iterator<Entry<String, Object>> iterator = condition.entrySet().iterator();iterator.hasNext();) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				
				//因为查询条件改为了and，所以当条件为空字符串的时候不向查询条件中拼写
				if(value != null && !"".equals(value.toString().trim())){
					if("id".equals(key)) {
						bsons.add(Filters.eq("_id", new ObjectId(value.toString())));
					}else if ("articleId".equals(key)) {
						bsons.add(Filters.eq("articleId", value));
					}else if ("insertTimeStart".equals(key)) {
						bsons.add(Filters.gte("insertTime", value));
					}else if ("insertTimeEnd".equals(key)) {
						bsons.add(Filters.lte("insertTime", value));
					}else if ("page".equals(key) || "rows".equals(key)) {
						//这两个参数是分页参数，在分页查询数据时会用到，但是在查询总条数的时候并不会用到，但是也不能拼接到查询语句中
					}else {
						//其他的进行右模糊查询
						//参考下regex的使用
						//i  　如果设置了这个修饰符，模式中的字母会进行大小写不敏感匹配。
						//m   默认情况下，PCRE 认为目标字符串是由单行字符组成的(然而实际上它可能会包含多行).如果目标字符串 中没有 "\n"字符，或者模式中没有出现“行首”/“行末”字符，设置这个修饰符不产生任何影响。
						//s    如果设置了这个修饰符，模式中的点号元字符匹配所有字符，包含换行符。如果没有这个修饰符，点号不匹配换行符。
						//x    如果设置了这个修饰符，模式中的没有经过转义的或不在字符类中的空白数据字符总会被忽略，并且位于一个未转义的字符类外部的#字符和下一个换行符之间的字符也被忽略。 这个修饰符使被编译模式中可以包含注释。 注意：这仅用于数据字符。 空白字符 还是不能在模式的特殊字符序列中出现，比如序列 。
						//注：JavaScript只提供了i和m选项，x和s选项必须使用$regex操作符
						
						//在命令行的时候pattern左右使用//包起来，是因为通过//来表示包起来的是pattern，但是如果java中再将//拼接到字符串中，那么//就会当做pattern的一部分去匹配，就会出现问题
						//debug的时候，发现pattern中居然包括//这是不对的
						bsons.add(Filters.regex(key, "" + value.toString() + ".*", "i"));
					}
				}
				
			}
			
			//如果要在find中传入bson数组，那么bson数组必须不能为空
			if(bsons != null && bsons.size() > 0){
				count = collection.count(Filters.or(bsons));
			}else{
				count = collection.count();
			}
			mongoClient.close();
		}
		
		return count;
	}
	
	public void updateArticle(Article article){
		//To connect to a single MongoDB instance:
	    //You can explicitly specify the hostname and the port:
		MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
		                                         Arrays.asList(credential));
		//Access a Database
		MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
		
		//Access a Collection
		MongoCollection<Document> collection = database.getCollection("Article");
		
		//Create a Document
		 Document doc = (Document)SchemaDocumentUtil.schameToDocument(article, Article.class);
		 
		 //Update a Document
		 collection.updateOne(Filters.eq("_id", doc.get("_id")), new Document("$set", doc));
		 mongoClient.close();
	}
	
	public Article findArticleByArticleId(Integer articleId) {
		Article article = null;
		if(articleId != null) {
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
			                                         Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			List<Document> docs = collection.find(Filters.eq("articleId", articleId)).into(new ArrayList<Document>());
			if(docs != null && docs.size() > 0) {
				article = (Article)SchemaDocumentUtil.documentToSchame(docs.get(0), Article.class);
			}
			mongoClient.close();
		}
		
		return article;
	}
	
	public Article findArticleById(String id) {
		Article article = null;
		if(id!= null && !"".equals(id.trim())) {
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
			                                         Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			List<Document> docs = collection.find(Filters.eq("_id", new ObjectId(id))).into(new ArrayList<Document>());
			if(docs != null && docs.size() > 0) {
				article = (Article)SchemaDocumentUtil.documentToSchame(docs.get(0), Article.class);
			}
			mongoClient.close();
		}
		
		return article;
	}
	
	public void updateArticleMultiFieldsById(String id, Map<String, Object> map){
		
		if(map != null && map.size() > 0) {
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
					Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			//Create a Document
			Document doc = new Document();
			
			for(Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();iterator.hasNext();) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				
				doc.append(key, value);
			}
			
			//Update a Document
			collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", doc));
			mongoClient.close();
		}
	}
	
	public void updateArticleMultiFieldsByArticleId(Integer articleId, Map<String, Object> map){
		
		if(map != null && map.size() > 0) {
			//To connect to a single MongoDB instance:
			//You can explicitly specify the hostname and the port:
			MongoCredential credential = MongoCredential.createCredential(dataSource.getUser(), dataSource.getDbUserDefined(), dataSource.getPassword().toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(dataSource.getIp(), dataSource.getPort()),
					Arrays.asList(credential));
			//Access a Database
			MongoDatabase database = mongoClient.getDatabase(dataSource.getDatabase());
			
			//Access a Collection
			MongoCollection<Document> collection = database.getCollection("Article");
			
			//Create a Document
			Document doc = new Document();
			
			for(Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();iterator.hasNext();) {
				Entry<String, Object> entry = iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				
				doc.append(key, value);
			}
			
			//Update a Document
			collection.updateOne(Filters.eq("articleId", articleId), new Document("$set", doc));
			mongoClient.close();
		}
	}
	
}
