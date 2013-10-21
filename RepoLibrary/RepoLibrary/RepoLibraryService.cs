using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using MongoDB.Driver;
using MongoDB.Driver.Builders;
using System.Data.SqlClient;

namespace RepoLibrary
{
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single, ConcurrencyMode = ConcurrencyMode.Reentrant)]
    public class RepoLibraryService : IRepoLibrary
    {
        public RepoLibraryService()
        {
            // MongoDB obsolete database (WILL BE REMOVED SOON)
            //const string connectionString = "mongodb://localhost/?safe=true";
            //Db = new MongoDBDatabaseGateway(connectionString);
            const string connectionString = @"Data Source=localhost\SQLEXPRESS;Initial Catalog=shopDB;User ID=shopsGatewayLogin;Password=GiaeQgn7fee7o.-";
            Db = new MSSQLDatabaseGateway(connectionString);

            //Db = new MSSQLDatabaseGateway(connString2Builder.ToString());
        }

        public RepoLibraryService(IDatabaseGateway db)
        {
            Db = db;
        }

        public IDatabaseGateway Db { get; set; }

        public Project GetProject(string name)
        {
            return Db.GetProject(name);
        }

        public Project GetProjectByKey(string key)
        {
            return Db.GetProjectByKey(key);
        }

        public string SaveProject(Project data)
        {
            return Db.SaveProject(data);
        }

        public string ToString()
        {
            return "ololo";
        }
    }
}
