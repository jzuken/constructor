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
        Project GetProject(string name);

        [OperationContract]
        string SaveProject(Project data);
    }

    [DataContract]
    public class Project
    {
        [DataMember]
        public string Name { get; set; }

        [DataMember]
        public string Settings { get; set; }

    }
}
