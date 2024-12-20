CREATE TABLE pets (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      owner_id BIGINT NOT NULL,
                      name VARCHAR(50) NOT NULL,
                      species VARCHAR(50),
                      breed VARCHAR(50),
                      age INT,
                      gender ENUM('MALE', 'FEMALE'),
                      weight DECIMAL(5,2),
                      avatar MEDIUMBLOB, -- 头像列
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('CUSTOMER', 'DOCTOR', 'PHARMACIST', 'RECEPTIONIST', 'ADMIN') NOT NULL,
                       real_name VARCHAR(50),
                       phone VARCHAR(20),
                       email VARCHAR(100),
                       avatar MEDIUMBLOB, -- 头像列
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       status BOOLEAN DEFAULT TRUE,
                       last_login TIMESTAMP
);
CREATE TABLE doctors (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         specialization VARCHAR(100),
                         introduction TEXT,
                         title VARCHAR(50),
                         schedule_rules JSON,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE appointments (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              pet_id BIGINT NOT NULL,
                              doctor_id BIGINT NOT NULL,
                              appointment_time TIMESTAMP NOT NULL,
                              status ENUM('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'),
                              symptoms TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (pet_id) REFERENCES pets(id),
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);



-- 就诊记录表
CREATE TABLE medical_records (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 appointment_id BIGINT NOT NULL,  -- 关联预约ID
                                 pet_id BIGINT NOT NULL,         -- 关联宠物ID
                                 doctor_id BIGINT NOT NULL,      -- 关联医生ID
                                 diagnosis TEXT,                 -- 诊断结果
                                 treatment TEXT,                 -- 治疗方案
                                 prescription TEXT,              -- 处方
                                 visit_time TIMESTAMP NOT NULL,  -- 就诊时间
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (appointment_id) REFERENCES appointments(id),
                                 FOREIGN KEY (pet_id) REFERENCES pets(id),
                                 FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- 费用明细表
CREATE TABLE medical_expenses (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  record_id BIGINT NOT NULL,      -- 关联就诊记录ID
                                  item_name VARCHAR(100) NOT NULL, -- 项目名称
                                  item_type VARCHAR(50) NOT NULL,  -- 费用类型(诊疗/药品/检查等)
                                  amount DECIMAL(10,2) NOT NULL,   -- 金额
                                  quantity INT NOT NULL,           -- 数量
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (record_id) REFERENCES medical_records(id)
);




-- 9. 费用记录表(bills)
CREATE TABLE bills (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       medical_record_id BIGINT NOT NULL,
                       total_amount DECIMAL(10,2) NOT NULL,
                       payment_status ENUM('PENDING', 'PAID', 'REFUNDED'),
                       payment_method VARCHAR(50),
                       paid_at TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (medical_record_id) REFERENCES medical_records(id)
);




-- 在线咨询表
CREATE TABLE consultations (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               doctor_id BIGINT NOT NULL,
                               pet_id BIGINT NOT NULL,
                               status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'),
                               title VARCHAR(255),
                               description TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id),
                               FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                               FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- 咨询消息表
CREATE TABLE consultation_messages (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       consultation_id BIGINT NOT NULL,
                                       sender_id BIGINT NOT NULL,
                                       content TEXT,
                                       message_type ENUM('TEXT', 'IMAGE'),
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (consultation_id) REFERENCES consultations(id)
);

create table medicines
(
    id             bigint auto_increment
        primary key,
    name           varchar(255)                         null,
    specification  varchar(255)                         null,
    manufacturer   varchar(255)                         null,
    category       varchar(255)                         null,
    unit_price     decimal(38, 2)                       null,
    stock_quantity int                                  null,
    min_stock      int                                  null,
    status         tinyint(1) default 1                 null,
    created_at     timestamp  default CURRENT_TIMESTAMP null
);