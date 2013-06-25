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
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single, ConcurrencyMode=ConcurrencyMode.Reentrant)]
    public class RepoLibrary : IRepoLibrary
    {
        public RepoLibrary()
        {
            const string connectionString = "mongodb://localhost/?safe=true";
            var client = new MongoClient(connectionString);
            db = client.GetServer().GetDatabase("repo");
            collection = db.GetCollection<ProjectData>("projects");
        }

        public ProjectData GetProject(int id)
        {
            return collection.FindOne(Query.EQ("_id", id));
        }

        public string SaveProject(ProjectData data)
        {
            var result = collection.Save(data);
            if (result.Ok)
                return "ok";

            return result.Response.ToString();
        }

        public string ToString()
        {
            return "ololo";
        }

        private MongoDatabase db;
        private MongoCollection<ProjectData> collection;
    }
}
