db = db.getSiblingDB('packagesservicedb');
db.createCollection('packages');
db.database_sequences.insert({id: 'packages_sequence', seq: 1});
// use admin
// db.createUser(
//     {
//         user: "root",
//         pwd: "rootroot",
//         roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
//     }
// )
// db.createUser(
//     {
//         user: "dbUser",
//         pwd: "dbPassword",
//         roles: [ { role: "readWrite", db: "packagesservicedb" } ]
//     }
// )

// use packagesservicedb;