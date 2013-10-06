using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Xml;


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
                WebClient client = new WebClient();
                string returnCode = client.DownloadString("https://secure.x-cart.com/service.php?target=recurring_plans&password=pmh6_2lGTENNqewuhd&url=" + url);
                string expDate = "";
                XmlDocument xml = new XmlDocument();
                xml.LoadXml(returnCode);
                XmlNodeList plans = xml.GetElementsByTagName("plan");
                foreach (XmlNode plan in plans)
                {
                    bool isAdminPlan = false;
                    XmlNodeList children = plan.ChildNodes;
                    foreach (XmlNode child in children)
                    {
                        if (child.Name == "product_id")
                        {
                            if (child.InnerText == "473")
                            {
                                isAdminPlan = true;
                            }
                        }
                    }
                    if (isAdminPlan)
                    {
                        foreach (XmlNode child in children)
                        {
                            if (child.Name == "renewal_end")
                            {
                                expDate = child.InnerText;
                            }
                        }
                    }
                }
                RepoLibraryReference.Project project = wcfClient.GetProject(url);
                if (project != null)
                {
                    project.ExpirationDate = expDate;
                    wcfClient.SaveProject(project);
                }
                return new HttpResponseMessage() { Content = new StringContent(expDate)};
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
