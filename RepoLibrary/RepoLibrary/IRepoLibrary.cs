using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace RepoLibrary
{
    [ServiceContract]
    public interface IRepoLibrary
    {
        [OperationContract]
        Project GetProject(int id);

        [OperationContract]
        string SaveProject(Project data);
    }

    [DataContract]
    public class Project
    {
        [DataMember]
        public int Id { get; set; }

        [DataMember]
        public string Name { get; set; }

    }
}
