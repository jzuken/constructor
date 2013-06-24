using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace RepoLibrary
{
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single, ConcurrencyMode=ConcurrencyMode.Reentrant)]
    public class RepoLibrary : IRepoLibrary
    {
        public ProjectData GetProject(int id)
        {
            if (projects.ContainsKey(id))
                return null;
            
            return projects[id];
        }

        public void SaveProject(ProjectData data)
        {
            projects[data.ProjectId] = data;
        }

        Dictionary<int, ProjectData> projects = new Dictionary<int, ProjectData>();
    }
}
