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
        [ActionName("DefaultAction")]
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

        [ActionName("CheckSubscribtion")]
        public HttpResponseMessage GetCheckSubscribtion(string name)
        {
            RepoLibraryReference.Project project = wcfClient.GetProject(@name);
            if (project == null)
            {
                return new HttpResponseMessage() {Content = new StringContent("{}") };
            }
            else
            {
                string expirationDate = project.ExpirationDate;
                DateTime todate = DateTime.Today;
                DateTime expiring;
                bool parsed = DateTime.TryParse(expirationDate, out expiring);
                if (parsed)
                {
                    if (DateTime.Compare(todate, expiring) > 0)
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"expired\"}") };
                    }
                    else
                    {
                        return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"active\"}") };
                    }
                }
                else
                {
                    return new HttpResponseMessage() { Content = new StringContent("{\"subscribed\": \"none\"}") };
                }
            }
        }
    }
}