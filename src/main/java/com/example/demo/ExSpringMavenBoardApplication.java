package com.example.demo;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
/*
 * @MapperScan: Mapper 인터페이스 인식을 위한 설정
 * */
@SpringBootApplication
@MapperScan(value = {"com.example.demo.board.mapper"})
public class ExSpringMavenBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExSpringMavenBoardApplication.class, args);
	}

	/* 
	 * SqlSessionFactory 설정
	 * @Bean: 스프링에 필요한 객체를 생성
	 * sqlSessionFactory(DataSource dataSource): MyBatis의 SqlSessionFactory를 반환 함
	 * 스프링부트 실행 시, DataSource 객체를 생성하고 데이터베이스 주입, 결과 반환 하여 사용
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource);
		return sessionFactory.getObject();
	}
}
