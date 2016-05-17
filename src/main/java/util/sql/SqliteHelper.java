/**@author:idevcod@163.com
 * @date:2015年12月20日下午11:52:08
 * @description:<TODO>
 */
package util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.StringUtil;

public class SqliteHelper
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String DB_NAME = "GenerateID.db";
    private final String JDBC_PREFIX = "jdbc:sqlite:";
    private final static SqliteHelper instance = new SqliteHelper();

    private SqliteHelper()
    {

    }

    public static SqliteHelper getInstance()
    {
        return instance;
    }

    public void onCreate()
    {
        getConnection();
    }

    private Connection getConnection()
    {
        return getConnection(DB_NAME);
    }

    /**
     * @author:idevcod@163.com
     * @date:2015年12月21日上午12:24:22
     * @description:<TODO>
     * @param connectionName
     * @return connection or null
     */
    private Connection getConnection(String connectionName)
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(JDBC_PREFIX + connectionName);
        } catch (SQLException e)
        {
            logger.error("Get connection failed,exception msg is {}", e.getMessage());
        }

        return connection;
    }

    /**
     * @author:idevcod@163.com
     * @date:2015年12月27日下午9:07:28
     * @description:<TODO>
     * @param sql
     * @return true if execute success.false if sql statement is invalid or
     *         encounter sql exception
     */
    public boolean execute(String sql)
    {
        if (!isSqlValid(sql))
        {
            logger.debug("sql statement is invalid.");
            return false;
        }

        try (Connection connection = getConnection(); Statement statement = connection.createStatement())
        {
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.execute(sql);
        } catch (SQLException e)
        {
            logger.error("exceute sql statement failed. Error msg is {}", e.getSQLState());
            return false;
        }

        return true;
    }

    /***
     * @author:idevcod@163.com
     * @date:2016年5月17日下午11:05:20
     * @description:查询数据库中的数据，调用者通过实现DataFetcher接口来对查询到的数据进行处理。
     * @param sql
     * @param dataFetcher
     */
    public void executeQuery(String sql, DataFetcher dataFetcher)
    {
        if (!isSqlValid(sql))
        {
            logger.error("invalid sql in executeQuery.");
            return;
        }

        try (Connection connection = getConnection(); Statement statement = connection.createStatement();ResultSet resultSet = statement.executeQuery(sql);)
        {
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            
            logger.debug("query sql is {}", sql);
            dataFetcher.fetch(resultSet);
        } catch (SQLException e)
        {
            logger.error("exceute sql statement failed. Error msg is {}", e.getMessage());
            return;
        }
    }

    private boolean isSqlValid(String sql)
    {
        if (StringUtil.isEmpty(sql))
        {
            return false;
        }

        // TODO 需要完善规则。
        return true;
    }

}
