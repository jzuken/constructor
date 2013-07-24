using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace RepoLibraryServiceWebRole
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "RepoLibrary" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select RepoLibrary.svc or RepoLibrary.svc.cs at the Solution Explorer and start debugging.
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
            this.Db = new MSSQLDatabaseGateway(connString2Builder.ToString());
        }

        public RepoLibrary(IDatabaseGateway db)
        {
            this.Db = db;
        }


        public string GetData(int value)
        {
            return string.Format("You entered: {0}", value);
        }

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
            return "RepoLibrary WCF service";
        }

        public IDatabaseGateway Db { get; set; }
    }
}
