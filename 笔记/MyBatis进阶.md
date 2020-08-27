# MyBatis进阶

## [1.数据库环境切换](#1)

## [2.注解方式](#2)

## [3.增删改的返回值问题](#3)

## [4.Oracle事务提交](#4)

## [5.自增](#5)

## [6.参数问题](#6)

## [7.增加null](#7)

## [8.返回值为HashMap](#8)

## [9.ResuletMap](#9)

## [10.别名问题](#10)

## [11.SQL标签](#11)

## [12.内置函数](#12)

## [13.模糊查询](#13)

## [14.设计思想（架构）](#14)

## [15.源码解析](#15)

## [16.自定义拦截器](#16)

## [17.批量操作DML](#17)

## [18.PageHelper插件实现分页](#18)



## <span id = "1">1.数据库环境切换</span>

### （1）切换environment

在conf.xml中，指定实际使用的数据库

```xml
<environments default="developmentOracle">
	<!--Oracle数据库-->
	<environment id="developmentOracle">
		<transactionManager type="JDBC" />
		<dataSource type="POOLED">		
			<property name="driver" value="${oracle.driver}" />
			<property name="url" value="${oracle.url}" />
			<property name="username" value="${oracle.username}" />
			<property name="password" value="${oracle.password}" />
		</dataSource>
	</environment>

	<!--MySQL数据库-->
	<environment id="developmentMySQL">
		<transactionManager type="JDBC" />
		<dataSource type="POOLED">
			<property name="driver" value="${mysql.driver}" />
			<property name="url" value="${mysql.url}" />
			<property name="username" value="${mysql.username}" />
			<property name="password" value="${mysql.password}" />
		</dataSource>
	</environment>
</environments>
```

### （2）databaseIdProvider标签

在conf.xml中，通过databaseIdProvider配置数据库支持类，并通过子标签property指定数据库别名

```xml
<!-- 配置数据库支持类-->
<databaseIdProvider type="DB_VENDOR">
	<property name="Oracle" value="oracle"/>
	<property name="MySQL" value="mysql"/>
</databaseIdProvider>
```

### （3）数据库的SQL语句

通过databaseId指定数据库一般用在具有特性的sql语句中，例如rownum在Oracle数据库中可以使用，又如limit在MySQL数据库中可以使用。

```xml
<select id="queryByNo" parameterType="int" resultType="student" databaseId="oracle">
	select * from student where stuno = #{stuno}
</select>

<select id="queryByNo" parameterType="int" resultType="student" databaseId="mysql">
	select * from student where stuno = #{stuno}
</select>
```

如果mapper.xml的sql标签仅有一个不带databaseId的标签，则该标签会自动适应当前数据库
如果既有不带databaseId的标签，又有带databaseId的标签，则程序会优先使用带databaseId的标签

### （4）在mappe.xml中指定数据库

通过增删改查标签中的databaseId属性来指定

```xml
<select id="queryByNo" parameterType="int" resultType="student" databaseId="oracle">
	select * from student where stuno = #{stuno}
</select>
```

## <span id = "2">2.注解方式</span>

推荐使用xml配置

### （1）sql语句写在接口的方法上

```java
package nuc.hzb.mapper;

import nuc.hzb.entity.Student;

import org.apache.ibatis.annotations.Select;

// 接口的编写需要根据约定：（studentMapper.xml）
public interface StudentMapper {

    @Select("select * from student where stuno = #{stuno}")
    Student queryByNoWithAnnotate(int stuNo);

}
```

### （2）修改conf.xml

将接口的全类名写入<mapper>标签中，让MyBatis知道sql语句此时是存储在接口中

需要注意的是注解方式是通过class去指定，而xml配置方式是通过resource去指定

**注解和xml都支持批量引入<package name="nuc.hzb.mapper"/>**

不建议将配置xml和注解两种方式混合使用

```xml
<configuration>	

    ...
    
	<mappers>
		<!--<mapper resource="nuc/hzb/mapper/studentMapper.xml" />-->
		<mapper class="nuc.hzb.mapper.StudentMapper"></mapper>
        <!--通过package标签可以全部识别-->
        <!--<package name="nuc.hzb.mapper"/>-->
	</mappers>
</configuration>
```

## <span id = "3">3.增删改的返回值问题</span>

返回值：可以是void、Integer、Long、Boolean

操作：只需要在接口中修改返回值即可

```java
boolean addStudent(Student student);
```

## <span id = "4">4.Orcale事务提交</span>

### （1）手动提交

```java
SqlSession session = sessionFactory.openSession();
...
session.commit();
session.close();
```

### （2）自动提交

```java
SqlSession session = sessionFactory.openSession(true);
```

## <span id = "5">5.自增</span>

### （1）MySQL

MySQL支持自增

可以在图形界面设置，也可以通过sql语句设置，自增的字段必须是该表的主键

```xml
<insert id="addStudent" parameterType="student" databaseId="mysql"
			useGeneratedKeys="true" keyProperty="stuNo">
  insert into student(stuname, stuage, graname) values(#{stuName}, #{stuAge}, #{graName})
</insert>
```

```java
public static void addStudent() throws IOException {
	Reader reader = Resources.getResourceAsReader("conf.xml");
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
   	SqlSession session = sessionFactory.openSession(true);
	Student student = new Student();
    student.setStuName("小白");
    student.setStuAge(18);
    student.setGraName("aaa");
    System.out.println(student);    
    StudentMapper studentmapper = session.getMapper(StudentMapper.class);   
    boolean flag = studentmapper.addStudent(student);   
    if (flag == true) {   
        System.out.println("增加成功" );       
        System.out.println(student);       
    } else {    
        System.out.println("增加失败" );       
    }    
    // 手工提交    
    // session.commit();  
    session.close();
}
```

### （2）Oracle

Oracle本身不支持自增，但是可以通过序列来模拟自增

序列自带的两个属性
nextval：序列中下一个值
currval: 当前值

#### ①方法一（推荐）

create sequence myseq increment by 1 start with 1;

通过<insert>的子标签<selectKey>实现

在<selectKey>中查询下一个序列（自增后的值），再将此值传入keyProperty="stuNo"属性，最后在真正执行时使用该属性值

```xml
<insert id="addStudentByOracle" parameterType="student" databaseId="oracle">
	<selectKey keyProperty="stuNo" resultType="Integer" order="BEFORE">
		select myseq.nextval from dual
	</selectKey>
	insert into student(stuno, stuname, stuage, graname) values(#{stuNo}, #{stuName}, #{stuAge}, #{graName})
</insert>
```

#### ②方法二

```xml
<insert id="addStudentByOracle" parameterType="student" databaseId="oracle">
	<selectKey keyProperty="stuNo" resultType="Integer" order="AFTER">
		select myseq.currval from dual
	</selectKey>
	insert into student(stuno, stuname, stuage, graname) values(myseq.nextval, #{stuName}, #{stuAge}, #{graName})
</insert>
```

## <span id = "6">6.参数问题</span>

### （1）通过一个JavaBean传值

多个参数封装到一个JavaBean中，通过通过一个JavaBean传值

```xml
<insert id="addStudentByOracle" parameterType="student" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{stuNo}, #{stuName}, #{stuAge}, #{graName})
</insert>
```

### （2）通过多个参数传值

当传入多个参数（8种基本数据类型），就不需要parameterType属性指定传入类型了

```xml
<insert id="addStudentByParams" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
		values(#{stuNo}, #{stuName}, #{stuAge}, #{graName})
</insert>
```

```java
boolean flag = studentmapper.addStudentByParams(100, "h", 15, "hh");
```

通过以上方式测试后，发现存在异常

```
Caused by: org.apache.ibatis.binding.BindingException: Parameter 'stuNo' not found. Available parameters are [arg3, arg2, arg1, arg0, param3, param4, param1, param2]
```

根据异常提示，修改insert标签中的sql语句

```xml
<!--arg是从0开始-->
<insert id="addStudentByParams" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{arg0}, #{arg1}, #{arg2}, #{arg3})
</insert>

<!--param是从1开始-->
<insert id="addStudentByParams" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{param1}, #{param2}, #{param3}, #{param4})
</insert>
```

也可以将arg或param变为指定的名称

通过@Param("xxx")指定参数的命名，在接口中通过@Param("sNo") 指定学号参数的名字

```java
boolean addStudentByParams(@Param("sNo") Integer stuNo, @Param("sName") String stuName, @Param("sAge") Integer stuAge, @Param("gName") String graName);
```

```xml
<insert id="addStudentByParams" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{sNo}, #{sName}, #{sAge}, #{gName})
</insert>
```

### （3）基本类型和对象类型混合

接口通过@Param("xxx")指定参数的名字（主要是为了方便操作，不指定也可以）

```java
boolean addStudentByNoAndObject(@Param("sNo") Integer stuNo, @Param("stu") Student student);
```

基本类型就通过指定的参数名字写入values中，如果是对象类型则通过指定的参数名获取对象后再取它的属性值

```xml
<insert id="addStudentByNoAndObject" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{sNo}, #{stu.stuName}, #{stu.stuAge}, #{stu.graName})
</insert>
```

测试

```java
Student student = new Student();
student.setStuName("小白");
student.setStuAge(18);
student.setGraName("yyy");
boolean flag = studentmapper.addStudentByNoAndObject(141, student);
```

## <span id = "7">7.增加null</span>

### （1）Oracle

如果插入的字段是Null，提示错误：需要OTHER类型而不是null

### （2）MySQL

如果插入的字段是Null，可以正常执行（没有约束）

### （3）原因

**各个数据库在MyBatis中对各种数据类型的默认值不一致**

MyBatis中，jdbcTypeForNull（如果是null），则默认值OTHER

对于OTHER来说，MySQL能够处理（NULL），但是Oracle不行

### （4）解决Oracle插入null

#### ①修改具体的sql标签

当某个数据类型Oracle无法处理时，告诉它用默认值null

注意，此时设置的jdbcType=NULL不会影响正常的赋值（“hzb”）

```xml
<insert id="addStudentByParams" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) 
    	values(#{sNo}, #{sName, jdbcType=NULL}, #{sAge}, #{gName})
</insert>
```

```java
boolean flag = studentmapper.addStudentByParams(1551, null, 15, "hh");
```

#### ②配置MyBatis全局配置文件conf.xml（推荐）

```xml
...
<properties resource="db.properties"/>
	
<settings>
	<setting name="jdbcTypeForNull" value="NULL"/>
</settings>

<typeAliases>
	<package name="nuc.hzb.entity"/>
</typeAliases>
...
```

**null→jdbcTypeForNull→NULL**

## <span id = "8">8.返回值为HashMap</span>

### （1）返回一个结果

一种是给select标签种的sql字段加上别名，这样通过HashMap获取别名即可取到对应的值

如果不加别名，则需要通过字段名（必须大写，Oracle的元数据（字段名、表名 ）都是大写）获取

```xml
<select id="queryStudentOutByHashMap" parameterType="int" resultType="HashMap">
	select stuNo "no", stuName "name", stuAge "age" from student where stuNo = #{stuNo}
</select>

<select id="queryStudentOutByHashMap" parameterType="int" resultType="HashMap">
	select stuNo, stuName, stuAge from student  where stuNo = #{stuNo}
</select>
```

```java
HashMap<String, Object> map = studentmapper.queryStudentOutByHashMap(141);
System.out.println(map);
// System.out.println(map.get("STUNAME"));
System.out.println(map.get("name"));
System.out.println(map.get("age"));
```

### （2）返回多个结果

需要在接口中指定哪个参数作为HashMap集合的k，如果在sql中指定别名，则在接口中的注解也需要写别名（与返回一个结果的使用方法一致）

```java
@MapKey("STUNO")
HashMap<Integer, Student> queryStudentsOutByHashMap();
```

```xml
<select id="queryStudentsOutByHashMap" resultType="HashMap">
	select stuNo, stuName, stuAge from student
</select>
```

```java
HashMap<Integer, Student> map = studentmapper.queryStudentsOutByHashMap();
```

## <span id = "9">9.ResultMap</span>

通过resultmap的映射，将程序中的字段与数据库中的字段名一一对应

column属性指定数据库中的字段名，大小写没有影响，但是如果写错了则程序会按照数据库中的字段名全大写返回，如果正确则程序中通过property指定的属性名获取对应的取值

```xml
<resultMap id="TestHashMap" type="HashMap">
<!--生成主键：alter table student add constraint pk_student_stuno primary key(stuno);-->
	<id column="stuno" property="stuNo" />
	<result column="stunAme" property="stuName"/>
    <result column="STUAGE1" property="stuAge"/>
</resultMap>
<select id="queryStudentsWithResultMap"  resultMap="TestHashMap">
	select stuNo, stuName, stuAge from student
</select>
```

```java
// 因为在映射中，column指定的属性名，数据库中存在，所以程序中使用property指定的属性名"stuNo"
@MapKey("stuNo")
HashMap<Integer, Student> queryStudentsWithResultMap();
```

在resultMap中还可以使用鉴别器：对相同sql中不同字段值进行判断，从而进行不同的处理

鉴别器

```xml
<resultMap id="studentResultMap" type="student">
	<id column="sno" property="stuNo"/>
    <result  column="sage" property="stuAge"/>
    <result  column="gname" property="graName"/>
	<!-- 鉴别器  : 对查询结果进行分支处理： 如果是a年级，则真名，如果b年级，显示昵称-->
    <discriminator javaType="string"  column="gname">
   		<case value="a" resultType="student" >
			<result  column="sname" property="stuName"/>
		</case>
		<case value="b" resultType="student">
			<result  column="nickname" property="stuName"/>
		</case>
	</discriminator>
</resultMap>
```

## <span id = "10">10.别名问题</span>

通过typeAliases标签可以设置别名，以后的输入参数就可以写不带包名的类名了，并且不区分大小写

这个包包含这个包下的类以及所有的子包下的类

```xml
<configuration>
...
	<properties resource="db.properties"/>

	<settings>
		<setting name="jdbcTypeForNull" value="NULL"/>
	</settings>

	<typeAliases>
		<package name="nuc.hzb.entity"/>
	</typeAliases>
...
</configuration>
```

如果在批量设置别名时，出现了冲突，可以使用@Alias("MyStudent")区分

在nuc.hzb.entity的子包下也存在一个Student类，此使对这个类指定一个专门的别名

```java
package nuc.hzb.entity.test;

import org.apache.ibatis.type.Alias;

@Alias("MyStudent")
public class Student {


}
```

## <span id = "11">11.SQL标签</span>

### （1）处理and

#### 方法一（1=1）

传入学生，返回学生，由于if标签中都有and，第一个and拼接后导致sql语法错误，此时通过1=1与后面的所有if标签拼接

```xml
<select id="queryStudentByNoWithONGL1" parameterType="student" resultType="student" databaseId="oracle">
	select * from student where 1=1
	<if test="stuName != null and stuName !='' ">
		and stuName like '%${stuName}%'
	</if>
	<if test="graName != null and graName !='' ">
		and graName like '%${graName}%'
	</if>
	<if test="stuAge != null and stuAge !='' ">
		and stuAge = #{stuAge}
	</if>
</select>
```

#### 方法二（where标签）

<where>可以处理拼接sql中开头的第一个and

```xml
<!--where标签智能判断第一个and，如果是第一个直接忽略，如果后面其它的and则保留and-->
<select id="queryStudentByNoWithONGL2" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<where>
		<if test="stuName != null and stuName !=''">
			and stuName like '%${stuName}%'
		</if>
		<if test="graName != null and graName !=''">
			and graName like '%${graName}%'
		</if>
		<if test="stuAge != null and stuAge !=''">
			and stuAge = #{stuAge}
		</if>
	</where>
</select>
```

#### 方法三（trim标签）

<trim>可以处理拼接sql中开头或结尾第一个and

简单理解<trim>：prefix、suffix用来添加sql中缺少的关键字，prefixOverrides、suffixOverrides用来去除sql中多余的关键字

##### 处理开头的and

```xml
<select id="queryStudentByNoWithONGL3" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<trim prefix="where" prefixOverrides="and">
		<if test="stuName != null and stuName !=''">
			and stuName like '%${stuName}%'
		</if>
		<if test="graName != null and graName !=''">
			and graName like '%${graName}%'
		</if>
		<if test="stuAge != null and stuAge !=''">
			and stuAge = #{stuAge}
		</if>
	</trim>
</select>
```

##### 处理结尾的and

```xml
<select id="queryStudentByNoWithONGL3" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<trim prefix="where" suffixOverrides="and">
		<if test="stuName != null and stuName !=''">
			stuName like '%${stuName}%' and
		</if>
		<if test="graName != null and graName !=''">
			graName like '%${graName}%' and
		</if>
		<if test="stuAge != null and stuAge !=''">
			stuAge = #{stuAge} and
		</if>
	</trim>
</select>
```

#### 测试接口

```java
List<Student> queryStudentByNoWithONGL1(Student student);
List<Student> queryStudentByNoWithONGL2(Student student);
List<Student> queryStudentByNoWithONGL3(Student student);
List<Student> queryStudentByNoWithONGL4(Student student);
```

#### 测试主方法

```java
public static void queryStudentByNoWithONGL() throws IOException {
	Reader reader = Resources.getResourceAsReader("conf.xml");      
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    SqlSession session = sessionFactory.openSession();
    StudentMapper studentMapper = session.getMapper(StudentMapper.class);
    Student student = new Student();
    student.setStuName("白");
    student.setStuAge(18);
    List<Student> students1 = studentMapper.queryStudentByNoWithONGL1(student);
    List<Student> students2 = studentMapper.queryStudentByNoWithONGL2(student);
    List<Student> students3 = studentMapper.queryStudentByNoWithONGL3(student);
    List<Student> students4 = studentMapper.queryStudentByNoWithONGL4(student);
    System.out.println(students1);
    System.out.println(students2);
    System.out.println(students3);
    System.out.println(students4);
    session.close();
}
```

### （2）练习trim

```xml
<update id="updateStudentByNoWithONGL" parameterType="student">
	update student
		<trim prefix="set" suffixOverrides="," suffix="where">
			<if test="stuName != null and stuName != ''">
				stuName=#{stuName},
			</if>
			<if test="stuAge != null and stuAge != ''">
				stuAge=#{stuAge},
			</if>
			<if test="graName != null and graName != ''">
				graName=#{graName},
			</if>
		</trim>
	stuNo=#{stuNo}
</update>
```

## <span id = "12">12.内置参数</span>

_parameter：代表MyBatis的输入参数
_databaseId：代表当前数据库的名字

### （1）_parameter示例

```xml
<select id="queryStudentByNoWithONGL_Parameter" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<trim prefix="where" suffixOverrides="and">
		<if test="_parameter.stuName != null and _parameter.stuName !=' '">
			stuName like '%${_parameter.stuName}%' and
		</if>
		<if test="_parameter.graName != null and _parameter.graName !=' '">
			graName like '%${_parameter.graName}%' and
		</if>
		<if test="_parameter.stuAge != null and _parameter.stuAge !=' '">
			stuAge = #{_parameter.stuAge}
		</if>
	</trim>
</select>
```

```xml
<select id="queryByNo_Parameter" parameterType="int" resultType="student" databaseId="oracle">
	select * from student where stuno = #{_parameter}
</select>
```

### （2）_databaseId示例

仅仅是为了演示，实际不可能这么做

如果是Oracle查询传入的学号，如果是mysql查询传入的学号再加一的学生

测试过程中不需要在配置文件中修改数据库，只需要修改此处的if语句的数据库，再同一个数据库中查询

```xml
<select id="queryByNo_Parameter_DatabaseId" parameterType="int" resultType="student" >
	<if test="_databaseId == 'oracle'">
		select * from student where stuno = #{_parameter}
	</if>
	<if test="_databaseId == 'mysql'">
		select * from student where stuno = #{_parameter} + 1
	</if>
</select>
```

## <span id = "13">13.模糊查询</span>

### （1）${}

原样输出

stuName like '%${stuName}%' and

### （2）#{}

传值时，直接传

student.setStuName("%s%")

stuName like #{stuName}

```xml
<select id="queryStudentByNoWithFuzzy" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<trim prefix="where" suffixOverrides="and">
		<if test="stuName != null and stuName !=''">
			stuName like '%${stuName}%' and
		</if>
		<if test="graName != null and graName !=''">
			graName like #{graName} and
		</if>
		<if test="stuAge != null and stuAge !=''">
			stuAge = #{stuAge} and
		</if>
	</trim>
</select>
```

```java
public static void queryStudentByNoWithFuzzy() throws IOException {
	Reader reader = Resources.getResourceAsReader("conf.xml");       	
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
   	SqlSession session = sessionFactory.openSession();
    StudentMapper studentMapper = session.getMapper(StudentMapper.class);
   	Student student = new Student();
    student.setStuName("白");
    student.setGraName("%a%");
  	List<Student> students = studentMapper.queryStudentByNoWithFuzzy(student);
   	System.out.println(students);
 	session.close();
}
```

### （3）bind参数

```
<bind name="_Name" value="'%'+stuName+'%'"/>
```

通过bind将传入的stuName进行了处理：增加了%...%（理解为字符串的拼接）

```xml
<select id="queryStudentByNoWithFuzzy" parameterType="student" resultType="student" databaseId="oracle">
	select * from student
	<trim prefix="where" suffixOverrides="and">
		<bind name="_Name" value="'%'+stuName+'%'"/>
		<if test="stuName != null and stuName !=''">
			stuName like #{_Name} and
		</if>
		<if test="graName != null and graName !=''">
			graName like #{graName} and
		</if>
		<if test="stuAge != null and stuAge !=''">
			stuAge = #{stuAge} and
		</if>
	</trim>
</select>
```

## <span id = "14">14.设计思想（架构）</span>

### （1）接口层

开发中的接口不一定指的就是interface，而是广义的接口。可以本身就是一个接口，或者是一个方法或是一个类等等

### （2）数据处理层

处理的是sql语句：首先处理参数，然后是解析sql，其次是执行sql，最后是处理返回结果

sql执行是通过Executor对象来实现的

### （3）框架支撑层

在MyBatis的配置文件中，有连接池管理、事务管理、缓存机制等等（各种辅助功能）

### （4）引导层

sql语句的引导方式：xml配置、注解

## <span id = "15">15.源码解析</span>

### （1）通过学号查询学生

```java
public static void queryByNo() throws IOException {
    
	Reader reader = Resources.getResourceAsReader("conf.xml");
    
    // 1.获取SqlSessionFactory对象
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    
    // 2.做各种成员变量的初始化，获取SqlSession对象
	SqlSession session = sessionFactory.openSession();
    
	// 3.传入StudentMapper接口，返回该接口的mapper代理对象studentMapper
	StudentMapper studentMapper = session.getMapper(StudentMapper.class);
    
	// 4.通过mapper代理对象studentMapper，来调用StudentMapper接口中的方法，映射到配置文件执行sql
	Student student = studentMapper.queryByNo(14);
    
	System.out.println(student);
	session.close();
}
```

### （2）解析

#### ①获取SqlSessionFactory对象

parser解析器通过parse方法中的parseConfiguration方法获取Configuration对象

其中parseConfiguration()在configuration标签设置了properties、settings、environments等属性标签

将所有的配置信息放在了Configuration对象中

解析所有的XxxMapper.xml文件（解析其中的增删改查标签）

会将XxxMapper.xml中的select等标签解析成MappedStatement对象，MappedStatement就是select等标签

**核心是configuration，可以通过断点调试并查看返回的configuration对象**

可以发现MappedStatement存在于Configuration中，environment存在于Configuration中

Configuration又存在于DefaultSqlSessionFactory对象中

结论：所有的配置信息、增删改标签全部存在于Configuration中

SqlSessionFactory对象→DefaultSqlSessionFactory→Configuration→包含了一切配置信息

#### ②获取SqlSession对象

configuration.newExecutor(tx, execType)获取SimpleExecutor（Executor）

根据不同的类型execType，产生不同的Executor

通过(Executor) interceptorChain.pluginAll(executor)会对执行器进行拦截操作

executor = (Executor) interceptorChain.pluginAll(executor); 
// 装饰模式，能够使一个对象变得更加强大的对象

interceptorChain（拦截链）作用：以后如果我们要给MyBatis写自己的插件，就可以通过拦截器实现

第三方插件的使用：首先写插件然后再放入拦截器

返回DefaultSqlSession(configuration, executor, autoCommit)

SqlSession→openSession()→openSessionFromDataSource()→DefaultSqlSession→执行sql

#### ③获取XxxMapper对象

执行增删改查Mapper接口→MapperProxy<T>→invoke()

MapperProxy是InvocationHandler实现类，InvocationHandler是jdk动态代理接口


用到了动态代理模式：增删改查→代理对象（MapperProxy对象）→代理对象帮我们“代理执行”增删改查

XxxMapper代理对象：MapperProxy对象

mapperMethod.execute(sqlSession,args)：实际调用增删改查的方法，依靠了sqlSession中的configuration和executor等

处理增删改查方法的参数：method.convertArgsToSqlCommandParam(args)，如果参数是0个，reutrun null；如果参数是1，返回第一个；如果有多个，参数放入map集合中

查询方法：selectOne()→selectList()：configuration.getMappedStatement()即获取到用于增删改查的对象

boundSql：将写的sql和参数值进行了拼接后的对象，即最终能被真正执行的sql

执行sql是通过Executor

如果缓存中没有要查询的内容，则进入数据库真实查询：queryFromDatabase()

MyBatis使用的jdbc对象是PreparedStatement

底层执行增删改查：PreparedStatement的execute()

MyBatis底层在执行CRUD时可能会涉及到四个处理器：StatementHandler、ParameterHandler、TypeHandler、ResultSetHandler

StatementHandler（处理对象PreparedStatement的控制器）

ParameterHandler（处理参数的控制器）

TypeHandler（处理类型的控制器）

ResultSetHandler（处理结果集的控制器）

XxxMapper: SqlSession(configuration,executor,事务)、代理接口的对象(MapperInterface)、methodCache(存放查询缓存， 底层是CurrentHashMap)

## <span id = "16">16.自定义拦截器</span>

### （1）回忆

四个处理器：StatementHandler、ParameterHandler、TypeHandler、ResultSetHandler

### （2）自定义插件涉及四大核心对象

四大核心对象：StatementHandler、ParameterHandler、Executor、ResultSetHandler

都涉及到了拦截器用于增强，四大核心对象都包含了该增强操作

### （3）MyInterceptor

自定义插件的编写逻辑： 根据MyBatis规则编写一个拦截器，在拦截器内部加入自定义增强功能

#### ①编写拦截器以及签名注解

```java
package nuc.hzb.interceptors;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;


import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class MyInterceptor implements Interceptor {

    // 拦截
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("拦截方法！");
        // 放行
        Object proceed = invocation.proceed();
        System.out.println(proceed);
        return proceed;
    }

    // 插件
    @Override
    public Object plugin(Object target) {
        // 将拦截器中定义的增强功能和原来的核心对象合并起来，称为最终的核心对象
        Object wrap = Plugin.wrap(target, this);
        System.out.println(wrap);
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("设置属性：" + properties);
    }
}
```

#### ②配置conf.xml

configuration的子标签是有顺序的，在idea中，可以故意打错，根据提示查看子标签的具体位置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
 PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
 "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
...

	<typeAliases>
		...
	</typeAliases>

	<plugins>
		<plugin interceptor="nuc.hzb.interceptors.MyInterceptor">
			<property name="name" value="hzb"/>
			<property name="age" value="20"/>
		</plugin>
	</plugins>

	<environments default="developmentOracle">
		<environment>
		...
		</environment>
	</environments>
    
...
</configuration>
```

#### ③调用

```java
public static void queryByNo() throws IOException {
	Reader reader = Resources.getResourceAsReader("conf.xml");
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    SqlSession session = sessionFactory.openSession()
    StudentMapper studentMapper = session.getMapper(StudentMapper.class);
    Student student = studentMapper.queryByNo(14);
    System.out.println(student);
    session.close();
}
```

### （4）多个拦截器

编写多个拦截器时，执行顺序和plugins标签的配置顺序一致

```java
package nuc.hzb.interceptors;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;


import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class MyInterceptor1 implements Interceptor {

    // 拦截
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("拦截方法111111！");
        // 放行
        Object proceed = invocation.proceed();
        System.out.println(proceed);
        return proceed;
    }

    // 插件
    @Override
    public Object plugin(Object target) {
        // 将拦截器中定义的增强功能和原来的核心对象合并起来，称为最终的核心对象
        Object wrap = Plugin.wrap(target, this);
        System.out.println(wrap + "拦截器111");
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("设置属性222：" + properties);
    }
}
```

```java
package nuc.hzb.interceptors;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class MyInterceptor2 implements Interceptor {

    // 拦截
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("拦截方法222222！");
        // 放行
        Object proceed = invocation.proceed();
        System.out.println(proceed);
        return proceed;
    }

    // 插件
    @Override
    public Object plugin(Object target) {
        // 将拦截器中定义的增强功能和原来的核心对象合并起来，称为最终的核心对象
        Object wrap = Plugin.wrap(target, this);
        System.out.println(wrap + "拦截器222");
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("设置属性222：" + properties);
    }
}
```

```xml
<plugins>
	<plugin interceptor="nuc.hzb.interceptors.MyInterceptor1">
		<property name="name" value="hzb"/>
		<property name="age" value="20"/>
	</plugin>

	<plugin interceptor="nuc.hzb.interceptors.MyInterceptor2">
		<property name="name" value="hzbhzb"/>
		<property name="age" value="20"/>
	</plugin>
</plugins>
```

### （5）自定义插件

查询学号返回学生的例子，通过拦截器实现查询14号返回13号学生

```java
package nuc.hzb.interceptors;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;


import java.sql.Statement;
import java.util.Properties;

//@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
@Intercepts({@Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class})})
public class MyInterceptor1 implements Interceptor {

    // 拦截
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("拦截方法111111！");

        // target就是具体的sql语句
        Object target = invocation.getTarget();
        System.out.println("目标对象：" + target);
        MetaObject metaObject = SystemMetaObject.forObject(target);
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println(value);
        
        metaObject.setValue("parameterHandler.parameterObject", 13);
        Object value1 = metaObject.getValue("parameterHandler.parameterObject");
        
        
        // 注意放行时机，必须在修改参数以后才可以放行
        Object proceed = invocation.proceed();
        System.out.println(value1);
        System.out.println(proceed);
        return proceed;
    }

    // 插件
    @Override
    public Object plugin(Object target) {
        // 将拦截器中定义的增强功能和原来的核心对象合并起来，称为最终的核心对象
        Object wrap = Plugin.wrap(target, this);
        System.out.println(wrap + "拦截器111");
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("设置属性222：" + properties);
    }
}

```

目标对象target包装后的产物→metaObject.getValue("可以从target中获取")

通过打印语句（目标对象：org.apache.ibatis.executor.statement.RoutingStatementHandler@103f852）

可知target就是RoutingStatementHandler→metaObject.getValue("可以从RoutingStatementHandler中获取")

metaObject.getValue("可以从RoutingStatementHandler中获取：boundSql/parameterHandler")

metaObject.getValue("parameterHandler")		

// XxxMapper.xml中的sql语句中的参数值
metaObject.getValue("parameterHandler.parameterObject")	

// XxxMapper.xml中的sql语句
metaObject.getValue("parameterHandler.boundSql")	

## <span id = "17">17.批量操作DML</span>

### （1）ExecutorType.BATCH

只需要添加ExecutorType.BATCH

sessionFactory.openSession(ExecutorType.BATCH );

实测：1W条数据，默认执行SIMPLE方式是：1833毫秒，BATCH执行方式是168毫秒

```xml
<insert id="addBatchStudents" parameterType="student" databaseId="oracle">
	insert into student(stuno, stuname, stuage, graname) values(#{stuNo}, #{stuName}, #{stuAge}, #{graName})
</insert>
```

```java
boolean addBatchStudents(Student student);
```

```java
public static void addBatchStudents() throws IOException {
	Reader reader = Resources.getResourceAsReader("conf.xml");
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    SqlSession session = sessionFactory.openSession(ExecutorType.BATCH);
    Student student = new Student();
    student.setStuName("abc");
    student.setStuAge(18);
    student.setGraName("AAA");
    StudentMapper studentmapper = session.getMapper(StudentMapper.class);
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
        student.setStuNo((int) (Math.random() * 9000) + 1000);
        studentmapper.addStudentByOracle(student);
    }
   	long end = System.currentTimeMillis();
    System.out.println(end-start);
    session.commit();
    session.close();
}
```

### （2）添加日志研究批量操作DML

#### ①log4j.jar

#### ② 配置conf.xml

```xml
<settings>
	<setting name="logImpl" value="LOG4J"/>
</settings>
```

#### ③log4j.properties

```properties
log4j.rootLogger=DEBUG, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

#### ④再次运行

发现1w条数据需要322毫秒，原因是加入日志会稍微影响效率

BATCH：预编译SQL一次，其余DML只需要设置参数值即可

```
...
DEBUG [main] - Opening JDBC Connection
DEBUG [main] - Checked out connection 1967892594 from pool.
DEBUG [main] - Setting autocommit to false on JDBC Connection [oracle.jdbc.driver.T4CConnection@754ba872]
DEBUG [main] - ==>  Preparing: insert into student(stuno, stuname, stuage, graname) values(?, ?, ?, ?) 
DEBUG [main] - ==> Parameters: 2517(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 7669(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 3272(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 9824(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 3769(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 3973(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 4414(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 6537(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 3142(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 2791(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - ==> Parameters: 3460(Integer), abc(String), 18(Integer), AAA(String)
...
```

我们再次将BATCH方式切换为默认的SIMPLE方式

发现1w条数据需要1793毫秒

```
...
DEBUG [main] - ==>  Preparing: insert into student(stuno, stuname, stuage, graname) values(?, ?, ?, ?) 
DEBUG [main] - ==> Parameters: 9244(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - <==    Updates: 1
DEBUG [main] - ==>  Preparing: insert into student(stuno, stuname, stuage, graname) values(?, ?, ?, ?) 
DEBUG [main] - ==> Parameters: 2711(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - <==    Updates: 1
DEBUG [main] - ==>  Preparing: insert into student(stuno, stuname, stuage, graname) values(?, ?, ?, ?) 
DEBUG [main] - ==> Parameters: 4572(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - <==    Updates: 1
DEBUG [main] - ==>  Preparing: insert into student(stuno, stuname, stuage, graname) values(?, ?, ?, ?) 
DEBUG [main] - ==> Parameters: 7256(Integer), abc(String), 18(Integer), AAA(String)
DEBUG [main] - <==    Updates: 1
...
```

#### ⑤结论

通过日志发现

BATCH：预编译SQL一次，其余DML只需要设置参数值即可

没有BATCH：预编译N次，每次DML都需要执行完整的SQL

### （3）拼接SQL（不推荐的方式）

#### ①Oracle批量插入

a.create table 表 select...from 旧表; 

b.insert into 表(...) select .. from 表(...);

c.begin...(DML)...end...;

d.数据泵、SQL Loader、外部表;

**begin...(DML)...end;为例**

命令行执行以下sql

```sql
begin
insert into student(stuno,stuname) values(1, 'hzbhzb');
insert into student(stuno,stuname) values(1, 'hzb');
end;
/
```

通过MyBatis实现

```xml
<insert id="addStudentsByOracle" parameterType="student" databaseId="oracle">
	<foreach collection="list" item="student" open="begin" close="end;">
		insert into student(stuno,stuname) values(#{student.stuNo}, #{student.stuName});
	</foreach>
</insert>
```

```java
Student student1 = new Student();
student1.setStuNo(1000);
student1.setStuName("AAA");
Student student2 = new Student();
student2.setStuNo(2000);
student2.setStuName("BBB");
List<Student> students = new ArrayList<>();
students.add(student1);
students.add(student2);
StudentMapper studentmapper = session.getMapper(StudentMapper.class);
studentmapper.addStudentsByOracle(students);
```

核心：将sql拼接成Oracle能够执行的sql，没有使用MyBatis自带的BATCH提高执行效率

注意：collection的参数必须是collection或List

#### ②MySQL批量插入

命令行执行

```sql
insert into student(stuno, stuname) values(100, 'AAA'), (200, 'BBB');
```

这种批量插入方式不推荐

a.没有用到MyBatis对批量插入的支持（BATCH）

b.不适合数据库迁移 （MySQL数据库独特的语句）

c.如果大量数据，则会将拼接的SQL语句拉的很长，而部分数据库对SQL语句的长度有限制

通过MyBatis实现

```xml
<insert id="addStudentsByMySQL" parameterType="student" databaseId="mysql">
	insert into student(stuno,stuname) values
	<foreach collection="list" separator="," item="student">
		(#{student.stuNo}, #{student.stuName})
    </foreach>
</insert>
```

还可以通过调存储过程、存储函数（可以使用） 

## <span id = "18">18.PageHelper插件实现分页</span>

### （1）jar

pagehelper.jar

jsqlparser.jar

注意版本的依赖问题

使用手册地址：https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md

### （2）配置conf.xml中

```xml
<plugins>
	<!-- com.github.pagehelper为PageHelper类所在包名 -->
	<plugin interceptor="com.github.pagehelper.PageInterceptor">
		<!-- 使用下面的方式配置参数，后面会有所有的参数介绍 -->
		<!--<property name="param1" value="value1"/>-->
	</plugin>
</plugins>
```

### （3）使用

#### ①Page

// Mapper接口方式的调用，推荐这种使用方式

PageHelper.startPage(第几页, 每页数据量);

```java
// 第二页，三条数据
Page<Object> page = PageHelper.startPage(2, 5);
List<Student> students = studentMapper.queryStudents();
for(Student student : students){
	System.out.println(student);
}


System.out.println("当前页：" + page.getPageNum());
System.out.println("总数据量：" + page.getTotal());
System.out.println("总页码：" + page.getPages());
System.out.println("页面大小：" + page.getPageSize());

```

// jdk8 lambda用法

```java
Page<Student> page = PageHelper.startPage(2, 3).doSelectPage(()-> studentMapper.queryStudents());
List<Student> students = page.getResult();
System.out.println(students);
for(Student student : students){
	System.out.println(student);
}
```

#### ②PageInfo

```java
PageHelper.startPage(2, 3);
List<Student> students = studentMapper.queryStudents();
PageInfo<Student> studentPageInfo = new PageInfo<>(students);
System.out.println(studentPageInfo.getList());
for(int pageNum : studentPageInfo.getNavigatepageNums()){
	System.out.println(pageNum);
}
```

官方使用案例

```java
//获取第1页，10条内容，默认查询总数count
PageHelper.startPage(1, 10);
List<User> list = userMapper.selectAll();
//用PageInfo对结果进行包装
PageInfo page = new PageInfo(list);
//测试PageInfo全部属性
//PageInfo包含了非常全面的分页属性
assertEquals(1, page.getPageNum());
assertEquals(10, page.getPageSize());
assertEquals(1, page.getStartRow());
assertEquals(10, page.getEndRow());
assertEquals(183, page.getTotal());
assertEquals(19, page.getPages());
assertEquals(1, page.getFirstPage());
assertEquals(8, page.getLastPage());
assertEquals(true, page.isFirstPage());
assertEquals(false, page.isLastPage());
assertEquals(false, page.isHasPreviousPage());
assertEquals(true, page.isHasNextPage());
```

