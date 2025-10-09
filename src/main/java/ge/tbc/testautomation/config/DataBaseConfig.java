package ge.tbc.testautomation.config;

import ge.tbc.testautomation.db.mapper.EmployeeMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class DataBaseConfig {

    public static EmployeeMapper dbMapper() {
        // Configure H2 DataSource
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost:9093/./companyDB;" +
                "DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;" +
                "MODE=MSSQLServer;DATABASE_TO_UPPER=FALSE;" +
                "CASE_INSENSITIVE_IDENTIFIERS=TRUE");
//        dataSource.setUsername("");
//        dataSource.setPassword("");

        // Transaction factory
        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        // MyBatis environment
        Environment environment = new Environment("development", transactionFactory, dataSource);

        // MyBatis configuration
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(EmployeeMapper.class);

        // Build SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        // Open session and return mapper
        SqlSession sqlSession = sqlSessionFactory.openSession(true); // auto-commit
        return sqlSession.getMapper(EmployeeMapper.class);
    }
}
