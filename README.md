# userAuthentication

#Setting up SQL database to work with project locally

Step 1) Create a databse called userAuth, "create DATABASE userAuth;" (the application will take care of tables)
Step 2) Create a user for databse called: testUser 
Step 3) Create password for testUser: password1
Step 4) "CREATE USER 'testUser'@'localhost' IDENTIFIED BY 'password1'; "
Step 5) Grant all privileges to testUser for that database, "GRANT ALL PRIVILEGES ON userAuth.* TO 'testUser'@'localhost' IDENTIFIED BY 'password1';"
