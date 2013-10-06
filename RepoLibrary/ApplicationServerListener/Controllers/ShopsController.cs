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
        public HttpResponseMessage Get(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return new HttpResponseMessage() {Content = new StringContent("{}") };
            }
            else
            {
                return new HttpResponseMessage() { Content = new StringContent(project.Settings) };
            }
        }
        /*
        public HttpResponseMessage GetURL(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return new HttpResponseMessage() {Content = new StringContent("{}") };
            }
            else
            {
                return new HttpResponseMessage() { Content = new StringContent(project.Settings) };
            }
        }
         * */
    }
}