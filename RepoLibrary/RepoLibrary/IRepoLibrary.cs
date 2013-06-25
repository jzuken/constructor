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
        ProjectData GetProject(int id);

        [OperationContract]
        string SaveProject(ProjectData data);
    }

    [DataContract]
    public class ProjectData
    {
        [DataMember]
        public int Id { get; set; }

        [DataMember]
        public string Name { get; set; }

    }
}
