Queryman Builder does only one thing - builds SQL query for PostgreSQL, but does it perfectly.
===

Queryman Builder provides a fluent Java API that allows to write SQL in object oriented way.


Documentation
=== 
*  [http://queryman.org](http://queryman.org)

Getting started
===
Queryman can be used to build as a SQL string in object-oriented way, as a 
`PreparedStatement`.

Queryman Builder does not require any configuration. Thus, everything you needed 
to to add it to dependency and you can immediately start to use it:

Gradle:
```
compile group: 'org.queryman', name: 'builder', version: '1.0.0'
```

Maven:
```
<dependency>
    <groupId>org.queryman</groupId>
    <artifactId>builder</artifactId>
    <version>1.0.0</version>
</dependency>
```

Build SQL query
====
Simple SQL query:
```
// SELECT * FROM book WHERE id = 15 AND price < 35.99 ORDER BY created_date LIMIT 10 OFFSET 50
Queryman.select("*")
    .from("book")
    .where("id", "=", 15)
        .and("price", "<", 35.99)
    .orderBy("created_date")
    .limit(10)
    .offset(50)
    .sql();
```

Prepared SQL query:
```
// SELECT * FROM book WHERE id = ? AND price < ? ORDER BY created_date LIMIT 10 OFFSET 50
// Parameters: 
//      1 -> 15
//      2 -> 35.99
Queryman.select("*")
    .from("book")
    .where("id", "=", 15)
        .and("price", "<", 35.99)
    .orderBy("created_date")
    .limit(10)
    .offset(50)
    .buildPreparedStatement(connection);
```

For details look at the docs.

Common gradle tasks
==
* `build`
* `test`
* `publishToMavenLocal`

 