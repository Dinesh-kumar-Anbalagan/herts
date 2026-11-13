package com.herts.dbService.mybatis;

import com.herts.model.InputOutputParams;
import com.herts.model.ListValues;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MybatisMapper {
    private final SqlSession sqlSession;

    @Autowired
    public MybatisMapper(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public InputOutputParams runQuery (String filename, String method, InputOutputParams input) {
        filename = filename+"."+method;

            if(method.equalsIgnoreCase( "findById")) {
                InputOutputParams inputOutputParams = (InputOutputParams) sqlSession.selectOne(filename, input);
                return inputOutputParams;
            }
            else if(method.equalsIgnoreCase("getDataId")){
                InputOutputParams inputOutputParams = (InputOutputParams) sqlSession.selectOne(filename, input);
                return inputOutputParams;
            }
            else if(method.equalsIgnoreCase("getData")){
                List<Object> list = sqlSession.selectList(filename, input);
                InputOutputParams lists = new ListValues();
                lists.setList(list);
                return lists;
            }
            else if(method.equalsIgnoreCase( "insert")) {
            Configuration config = sqlSession.getConfiguration();
            config.getMappedStatementNames().forEach(System.out::println);
            sqlSession.insert(filename, input);
            return input;
             }
            else if(method.equalsIgnoreCase(  "update")) {
                sqlSession.update(filename, input);
                return input;
            }
            else if(method.equalsIgnoreCase(  "delete")) {
                sqlSession.delete(filename, input);
                return input;
            }
            else {
                method.equals("findBYId");
                throw new UnsupportedOperationException("Unsupported method: " + method);
            }
    }
}
