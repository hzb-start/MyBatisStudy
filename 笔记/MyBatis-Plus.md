# MyBatis-Plus

## 1.作用

只做增强，不做改变（在MyBatis的基础上）

## 2.必备基础

Maven+Spring+MyBatis 

## 3.准备环境

### （1）jar

Maven工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>nuc.hzb</groupId>
    <artifactId>MyBatisPlusProject</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus</artifactId>
            <version>3.1.1</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>

        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>

        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.5.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>

    </dependencies>


</project>
```

### （2）数据库表与实体类

student（数据库表）、Student（实体类）

表和实体类可可以不需要一一对应

**@TableName(value = "student")**：指定数据库表的表名

**@TableField(exist = false)**：实体类有该属性，而数据库表中没有该字段

实体类中属性为驼峰格式，例如stuNo，在执行CRUD时候，数据库表中默认为stu_no

**一般建议就是类的是属性：stuName、表的字段：stu_name**

因此数据库表中如果是stu_no，则不需要专门指定

**@TableId(type = IdType.AUTO)**：可以用于回显数据库中自增的id数据

```java
package nuc.hzb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "student")
public class Student {

//    @TableId(value = "stuno", type = IdType.AUTO)
    @TableId(type = IdType.AUTO)
    private int stuNo;
//    @TableField(value = "stuname")
    private String stuName;
//    @TableField(value = "stuage")
    private int stuAge;

    @TableField(exist = false)
    private boolean stuSex;

    public Student() { }

    public Student(String stuName, Integer stuAge) {
        this.stuName = stuName;
        this.stuAge = stuAge;
    }

    public Student(int stuNo, String stuName, int stuAge) {
        this.stuNo = stuNo;
        this.stuName = stuName;
        this.stuAge = stuAge;
    }

    public int getStuNo() {
        return stuNo;
    }

    public void setStuNo(int stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuAge() {
        return stuAge;
    }

    public void setStuAge(int stuAge) {
        this.stuAge = stuAge;
    }

    public boolean isStuSex() {
        return stuSex;
    }

    public void setStuSex(boolean stuSex) {
        this.stuSex = stuSex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuNo=" + stuNo +
                ", stuName='" + stuName + '\'' +
                ", stuAge=" + stuAge +
                '}';
    }
}

```

### （3）MyBatis配置文件

mybatis.xml （没有具体配置信息，因为可以放入到Spring中配置）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
<!--        开启日志，首先扫描log4j，不开启程序会一一匹配-->
        <setting name="logImpl" value="LOG4J" />
<!--        禁用驼峰格式-->
<!--        <setting name="mapUnderscoreToCamelCase" value="false" />-->
    </settings>
</configuration>
```

### （4）日志log4j.properties

```properties
log4j.rootLogger=DEBUG, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

### （5）数据库的连接信息

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mp
jdbc.username=root
jdbc.password=Huang

#jdbc.driver=oracle.jdbc.OracleDriver
#jdbc.url=jdbc:oracle:thin:@localhost:1521:ORCL
#jdbc.username=scott
#jdbc.password=tiger
```

### （6）Spring配置文件

切换到MyBatis-Plus只需要将

```xml
<bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean" >
...
</bean>
```

改为

```xml
 <bean id="sqlSessionFactoryBean" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean" >
 ...
 </bean>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--数据源-->
    <context:property-placeholder location="classpath:db.properties" />

    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}" />
        <property name="jdbcUrl" value="${jdbc.url}" />
        <property name="user" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
    </bean>

    <bean id="dataSourceTransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <tx:annotation-driven transaction-manager="dataSourceTransactionManager" />

<!--    <bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean" >-->
    <bean id="sqlSessionFactoryBean" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean" >
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:mybatis.xml" />
        <property name="typeAliasesPackage" value="nuc.hzb.entity" />
    </bean>

    <!--studentMapper.方法-->
    <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="nuc.hzb.mapper" />
    </bean>
</beans>
```

## 4.CRUD操作对比

JDBC：Dao接口、Dao实现类

MyBatis：Mapper接口——SQL映射文件

MyBatis-Plus：Mapper接口

接口extends BaseMapper，之后无需编写SQL映射文件

```java
package nuc.hzb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import nuc.hzb.entity.Student;

public interface StudentMapper extends BaseMapper<Student> {

}
```

## 5.增删改查示例

```java
package nuc.hzb.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import nuc.hzb.entity.Student;
import nuc.hzb.mapper.StudentMapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    // 测试是否环境搭建成功
    public static void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ComboPooledDataSource dataSource = context.getBean("dataSource", ComboPooledDataSource.class);
        System.out.println(dataSource);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(connection);
    }

    public static void testInsert() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        Student student = new Student("hzb", 20);
        int insert = studentMapper.insert(student);
        System.out.println(insert);
        System.out.println(student);
    }

    public static void testDeleteById() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        int i = studentMapper.deleteById(5);
        System.out.println(i);
    }

    public static void testDeleteBatchIds() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        ArrayList<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        // DELETE FROM student WHERE stu_no IN ( ? , ? , ? )
        int i = studentMapper.deleteBatchIds(integers);
        System.out.println(i);
    }

    public static void testDeleteByMap() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stu_no", 6);
        map.put("stu_name", "hzbhzb");
        // DELETE FROM student WHERE stu_name = ? AND stu_no = ?
        int i = studentMapper.deleteByMap(map);
        System.out.println(i);
    }

    public static void testUpdateById() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        Student student = new Student(7, "hzbhzbhzb", 20);
        // UPDATE student SET stu_name=?, stu_age=? WHERE stu_no=?
        int i = studentMapper.updateById(student);
        System.out.println(i);
    }

    public static void testSelectById() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        // SELECT stu_no,stu_name,stu_age FROM student WHERE stu_no=?
        Student student = studentMapper.selectById(9);
        System.out.println(student);
    }

    public static void testQueryWrapper() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        QueryWrapper<Student> wrapper = new QueryWrapper<Student>();
        // SELECT stu_no,stu_name,stu_age FROM student WHERE stu_no BETWEEN ? AND ? OR stu_age >= ? AND stu_age <= ?
        // wrapper.between("stu_no", 7, 8).or().ge("stu_age", 30).le("stu_age", 40);
        // SELECT stu_no,stu_name,stu_age FROM student WHERE stu_no BETWEEN ? AND ? OR ( stu_age >= ? AND stu_age <= ? )
        // wrapper.between("stu_no", 7, 8).or(i -> i.ge("stu_age", 30).le("stu_age", 40));
        // SELECT stu_no,stu_name,stu_age FROM student WHERE stu_no BETWEEN ? AND ? OR ( stu_age >= ? AND stu_age <= ? ) AND stu_name LIKE ?
        wrapper.between("stu_no", 7, 8).or(i -> i.ge("stu_age", 30).le("stu_age", 40)).like("stu_name", "h");
        List<Student> students = studentMapper.selectList(wrapper);
        System.out.println(students);
    }

    public static void testUpdateWrapper() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        StudentMapper studentMapper = context.getBean(StudentMapper.class);
        Student student = new Student();
        student.setStuName("AAA");
        student.setStuAge(19);
        UpdateWrapper<Student> wrapper = new UpdateWrapper<Student>();
        wrapper.eq("stu_no", 9);
        // UPDATE student SET stu_name=?, stu_age=? WHERE stu_no = ?
        int update = studentMapper.update(student, wrapper);
        System.out.println(update);
    }


    public static void main(String[] args) {
//        testInsert();
//        testDeleteById();
//        testDeleteBatchIds();
//        testDeleteByMap();
//        testUpdateById();
//        testSelectById();
//        testQueryWrapper();
//        testUpdateWrapper();
    }
}
```

## 6.回忆步骤

（1）更换成MybatisSqlSessionFactoryBean

（2）继承一个父接口extends BaseMapper，之后就可以使用该接口中已经存在的CRUD方法

（3）通过注解将表（字段）和类（属性）要对应起来（或者按照约定的方式）

## 7.源码

MappedStatement对象就是select等标签

MyBatis/MP都是通过MappedStatement对象来指向增删改

预加载：MP启动时，会指定加载所有常见的CRUD语句（来自于MP提供的BaseMapper接口），并将这些语句封装到了MappedStatement对象中

## 8.AR编程

AR：activeRecoder，形式：通过实体类Student直接进行增删改查操作（不需要借助于Mapper对象）

是一种编写风格

```java
package nuc.hzb.entity.ar;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@TableName(value = "student")
public class StudentAR extends Model<StudentAR> {

    private int stuNo;
    private String stuName;
    private int stuAge;

    public StudentAR() { }

    public StudentAR(String stuName, int stuAge) {
        this.stuName = stuName;
        this.stuAge = stuAge;
    }

    public StudentAR(int stuNo, String stuName, int stuAge) {
        this.stuNo = stuNo;
        this.stuName = stuName;
        this.stuAge = stuAge;
    }

    public int getStuNo() {
        return stuNo;
    }

    public void setStuNo(int stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuAge() {
        return stuAge;
    }

    public void setStuAge(int stuAge) {
        this.stuAge = stuAge;
    }

    @Override
    public String toString() {
        return "StudentAR{" +
                "stuNo=" + stuNo +
                ", stuName='" + stuName + '\'' +
                ", stuAge=" + stuAge +
                '}';
    }
}
```

```java
public static void testAR() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentAR studentAR = new StudentAR("DDD", 20);
    studentAR.insert();
}
```

注意：使用时必须先加载IOC容器，目的是为了让AR知道要操作的是数据库在哪里

删除涉及id的话，需要在类中指明id

```java
@TableId(type = IdType.AUTO)
private int stuNo;
```

```java
public static void testAR() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentAR studentAR = new StudentAR();
	studentAR.setStuNo(11);
    studentAR.deleteById();
}
```

```java
public static void testAR() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentAR studentAR = new StudentAR();
	studentAR.deleteById(11);
}
```

```java
public boolean deleteById(Serializable id) {
...
}
```

MP将主键设置为了Serializable类型，目的：可以接受常见的类型：8个基本类型+String→Serializable

lambda举例

```java
public static void testAR() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentAR studentAR = new StudentAR();
    QueryWrapper<StudentAR> wrapper = new QueryWrapper<>();
    wrapper.lambda().like(StudentAR::getStuName, "h");
    List<StudentAR> studentARS = studentAR.selectList(wrapper);
    System.out.println(studentARS);
}
```

面向对象查询方式：查询的对象是“类的属性”

// like stu_name like '%a%'

wrapper.lambda().like(Student::getStuName, "a");

面向SQL查询方式：查询的对象是“表的字段”

// like stu_name like '%a%'

wrapper.like("stu_name", "a"); 

## 9.逆向工程（了解）

### （1）概念

逆向工程也可以称为代码生成器

MyBatis：student表→Student类、Mapper接口、mapper.xml

MyBatis-Plus：student表->Student类、Mapper接口、mapper.xml、Service、Controller

### （2）区别

MyBatis：根据模版配置文件生成

MyBatis-Plus：根据类生成

## 10.分页

### （1）拦截器实现

select等标签→MappedStatement对象

boundSql：将我们写的SQL和参数值进行了拼接后的对象，即最终能被真正执行的SQL

拦截器：编写拦截器、注入拦截器，放入plugins标签即可，可以对SQL进行“修改”

### （2）配置

```xml
<bean id="sqlSessionFactoryBean" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean" >
	...
	<property name="plugins">
		<list>
		<!--分页插件-->
		<bean class="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor" />
        </list>
	</property>
</bean>
```

### （3）测试

```java
public static void testPage() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentMapper studentMapper = context.getBean(StudentMapper.class);
	IPage<Student> studentIPage = studentMapper.selectPage(new Page<>(2, 3), null);
	System.out.println(studentIPage.getRecords());
	System.out.println(studentIPage.getCurrent());
	System.out.println(studentIPage.getTotal());
	System.out.println(studentIPage.getSize());
	System.out.println(studentIPage.getPages());
}
```

## 11.攻击SQL阻断解析器

阻止恶意（或者失误）的全表更新删除

```xml
<bean id="sqlSessionFactoryBean" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean" >
	<property name="plugins">
		<list>
		<!--分页插件，攻击SQL阻断解析器-->
		<bean class="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor">
			<property name="sqlParserList">
				<list>
					<bean class="com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser" />
				</list>
	</property>
		</bean>
		</list>
	</property>
</bean>
```

```java
public static void testDeleteAll() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentMapper studentMapper = context.getBean(StudentMapper.class);
    // Prohibition of full table deletion
	int delete = studentMapper.delete(null);
    System.out.println(delete);
}
```

## 12.性能分析插件

仍然写在plugins的list里面

```xml
<bean class="com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor">
	<property name="maxTime" value="100" />
	<!--格式化sql语句-->
    <property name="format" value="true" />
</bean>
```

该插件只用于开发环境（写代码，调试）

生产环境:  最终部署，交付给使用人员

性能测试：开发使用，部署关闭

## 13.乐观锁和悲观锁

乐观锁：cvs算法（总以为不冲突）

修改之前，要去检查，如果和当时取得值不一样，则修改失败

悲观锁：synchorinzed，lock（总以为冲突）

将并发操作变为串行操作

**一般用乐观锁，悲观锁影响性能**

```xml
<bean class="com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor" />
```

```java
@Version
private int version;

public int getVersion() {
	return version;
}

public void setVersion(int version) {
    this.version = version;
}
```

```java
public static void testUpdateByIdWithVersion() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	StudentMapper studentMapper = context.getBean(StudentMapper.class);
    Student student = new Student(10, "XXX", 99);
    // 此时数据中version为1
    student.setVersion(1);
    studentMapper.updateById(student);
}
```

修改后，数据库中version变为2

## 14.通过sql注入器自定义sql方法

```java
package nuc.hzb.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class MyDelete extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "delete from student where stu_no > 11";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, "deleteAllStudents", sqlSource);
    }
}
```

```java

package nuc.hzb.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import nuc.hzb.injector.methods.MyDelete;

import java.util.List;


public class MyInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = new DefaultSqlInjector().getMethodList();
        methodList.add(new MyDelete());
        return methodList;
        /*return Stream.of(
                new Insert(),
                new Delete(),
                new DeleteByMap(),
                new DeleteById(),
                new DeleteBatchByIds(),
                new Update(),
                new UpdateById(),
                new SelectById(),
                new SelectBatchByIds(),
                new SelectByMap(),
                new SelectOne(),
                new SelectCount(),
                new SelectMaps(),
                new SelectMapsPage(),
                new SelectObjs(),
                new SelectList(),
                new SelectPage(),
                new MyDelete()
        ).collect(toList());*/
    }
}

```

## 15.逻辑删除

```xml
<bean id="globalConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig">
	<property name="dbConfig">
        <bean class="com.baomidou.mybatisplus.core.config.GlobalConfig$DbConfig">
       		<property name="logicDeleteValue" value="1" />
            <property name="logicNotDeleteValue" value="0" />
        </bean>
    </property>
</bean>
```

```java
@TableLogic
private int logicDelete;

public int getLogicDelete() {
	return logicDelete;
}

public void setLogicDelete(int logicDelete) {
	this.logicDelete = logicDelete;
}
```

## 16.自动填充

```java
@TableField(fill = FieldFill.INSERT)
private String stuName;
```

```java
package nuc.hzb.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;


public class MyMetaObjectHandler  implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // stuName是类的属性名
        this.setInsertFieldValByName("stuName", "HZB", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
```

```xml
<bean id="metaObjectHandler" class="nuc.hzb.meta.MyMetaObjectHandler" />

<bean id="globalConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig">
	<property name="metaObjectHandler" ref="metaObjectHandler" />
</bean>
```
