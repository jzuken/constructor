﻿using System;
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
        Project GetProject(string url);

        [OperationContract]
        string SaveProject(Project data);
    }

    [DataContract]
    public class Project
    {
        [DataMember]
        public string Settings { get; set; }

        [DataMember]
        public string Url { get; set; }

        [DataMember]
        public string ExpirationDate { get; set; }
    }
}
