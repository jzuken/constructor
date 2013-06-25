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
            db = new DatabaseHandler(connectionString);
        }

        public Project GetProject(int id)
        {
            return db.GetProject(id);
        }

        public string SaveProject(Project data)
        {
            return db.SaveProject(data);
        }

        public string ToString()
        {
            return "ololo";
        }

        public void SetDbHandler(IDatabaseHander dbHandler)
        {
            db = dbHandler;
        }

        private IDatabaseHander db;
    }
}
