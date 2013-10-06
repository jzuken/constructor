using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;


namespace ApplicationServerListener.Controllers
{
    public class SubscriptionController : ApiController
    {
        RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
        [ActionName("DefaultAction")]
        public HttpResponseMessage Get()
        {
            NameValueCollection getParams = Request.RequestUri.ParseQueryString();
            if (getParams["url"] != null)
            {
                string url = getParams["url"].ToString();
                //wcfClient.GetProject(url).
                return new HttpResponseMessage() { Content = new StringContent(url)};
            }
            else if (getParams["old_url"] != null && getParams["new_url"] != null)
            {
                string oldUrl = getParams["old_url"].ToString();
                string newUrl = getParams["new_url"].ToString();
                return new HttpResponseMessage() { Content = new StringContent(oldUrl + " " + newUrl) };
            }
            return new HttpResponseMessage() { Content = new StringContent(Request.RequestUri.ToString()) };
        }
    }
}
