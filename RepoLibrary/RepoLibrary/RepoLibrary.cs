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
    public class RepoLibrary : IRepoLibrary
    {
        public RepoLibrary()
        {
            // MongoDB obsolete database (WILL BE REMOVED SOON)
            //const string connectionString = "mongodb://localhost/?safe=true";
            //Db = new MongoDBDatabaseGateway(connectionString);


            SqlConnectionStringBuilder connString2Builder;
            connString2Builder = new SqlConnectionStringBuilder();
            connString2Builder.DataSource = "tcp:e59bs4vft9.database.windows.net,1433";
            connString2Builder.InitialCatalog = "repo";
            connString2Builder.Encrypt = true;
            connString2Builder.TrustServerCertificate = true;
            connString2Builder.UserID = "repoLogin@e59bs4vft9";
            connString2Builder.Password = "4WqLF69E";
            Db = new MSSQLDatabaseGateway(connString2Builder.ToString());
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
