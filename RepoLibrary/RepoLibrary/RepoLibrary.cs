using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using MongoDB.Driver;
using MongoDB.Driver.Builders;

namespace RepoLibrary
{
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single, ConcurrencyMode = ConcurrencyMode.Reentrant)]
    public class RepoLibrary : IRepoLibrary
    {
        public RepoLibrary()
        {
            //const string connectionString = "mongodb://localhost/?safe=true";
            const string connectionString = @"Data Source=localhost\SQLExpress;Initial Catalog=repo;User ID=repoClient;Password=4WqLF69E";
            Db = new MSSQLDatabaseGateway(connectionString);
            //Db = new MongoDBDatabaseGateway(connectionString);
        }

        public RepoLibrary(IDatabaseGateway db)
        {
            Db = db;
        }

        public IDatabaseGateway Db { get; set; }

        public Project GetProject(int id)
        {
            return Db.GetProject(id);
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
