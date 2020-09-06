
-- 创建image_main表
CREATE TABLE
    t_article
    (
        id BIGINT NOT NULL AUTO_INCREMENT,
        title VARCHAR(255),
        content VARCHAR(1000),
        author VARCHAR(40),
		validStatus VARCHAR(2),
        insertTime DATETIME,
        updateTime DATETIME,
        PRIMARY KEY (id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
