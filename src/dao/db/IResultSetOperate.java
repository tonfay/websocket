package dao.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 */
public interface IResultSetOperate {

    //操作resultSet返回相应的数据
    Object getObject(ResultSet resultSet);

    Object getObject(Statement statement);
}
