
-- 创建image_main表
CREATE TABLE
    t_article
    (
        id serial,
        title VARCHAR(255),
        content VARCHAR(1000),
        author VARCHAR(40),
		validStatus VARCHAR(2),
        insertTime timestamp,
        updateTime timestamp,
        PRIMARY KEY (id)
    );



    
