using System;
using MongoDB.Driver;
using MongoDB.Driver.Builders;

namespace RepoLibrary
{
    class DatabaseHandler : IDatabaseHander
    {
        public DatabaseHandler(string connectionString)
        {
            var client = new MongoClient(connectionString);
            db = client.GetServer().GetDatabase("repo");
            collection = db.GetCollection<Project>("projects");
        }

        public Project GetProject(int id)
        {
            return collection.FindOne(Query.EQ("_id", id));
        }

        public string SaveProject(Project data)
        {
            var result = collection.Save(data);
            if (result.Ok)
                return "ok";

            return result.Response.ToString();
        }

        private MongoDatabase db;
        private MongoCollection<Project> collection;
    }
}
