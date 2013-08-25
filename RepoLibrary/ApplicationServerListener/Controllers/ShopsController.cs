using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ApplicationServerListener.Controllers
{
    public class ShopsController : ApiController
    {
        RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
        // GET api/values/5
        public string Get(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return "NOT FOUND";
            }
            else
            {
                return project.Settings;
            }
        }
    }
}