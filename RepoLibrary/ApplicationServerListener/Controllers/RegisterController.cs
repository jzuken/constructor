using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Collections.Specialized;
using System.Xml;
using System.Security.Cryptography;
using System.Text;

namespace ApplicationServerListener.Controllers
{
    public class RegisterInfo
    {
        public string ShopUrl { get; set; }
        public string ShopApiUrl { get; set; }
        public string ShopKey { get; set; }
    }

    public class RegisterController : ApiController
    {
        RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
       
        private string CreateMD5Hash(string input)
        {
            MD5 md5 = System.Security.Cryptography.MD5.Create();
            byte[] inputBytes = System.Text.Encoding.ASCII.GetBytes(input);
            byte[] hashBytes = md5.ComputeHash(inputBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hashBytes.Length; i++)
            {
                sb.Append(hashBytes[i].ToString("X2"));
            }
            return sb.ToString();
        }

        [ActionName("DefaultAction")]
        public HttpResponseMessage Post(RegisterInfo registerInfo)
        {
            string shopUrl = registerInfo.ShopUrl;
            string shopApiUrl = registerInfo.ShopApiUrl;
            string shopKey = registerInfo.ShopKey;
            string defaultSettgins = "{}";
            string shopKeyHash = this.CreateMD5Hash(shopKey);
            string shopExpirationDate = "";
            DateTime today = DateTime.Now;
            DateTime trialExpirationDate = today.AddDays(14);
            RepoLibraryReference.Project project = wcfClient.GetProject(shopUrl);
            if (project == null)
            {
                project = new RepoLibraryReference.Project();
                project.Url = shopUrl;
                project.Settings = defaultSettgins;
            }
            project.apiUrl = shopApiUrl;
            project.keyHash = shopKeyHash;
            project.ExpirationDate = shopExpirationDate;
            project.trialEndDate = trialExpirationDate.ToString();
            project.firstExpiredLogin = "Never";
            WebClient client = new WebClient();
            string returnCode = client.DownloadString("https://secure.x-cart.com/service.php?target=recurring_plans&password=pmh6_2lGTENNqewuhd&url=" + shopUrl);
            string expDate = "";
            string startDate = "";
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
                        if (child.Name == "renewal_start")
                        {
                            startDate = child.InnerText;
                        }
                    }
                }
            }
            project.ExpirationDate = expDate;
            project.subscribtionStartDate = startDate;
            wcfClient.SaveProject(project);
            project = wcfClient.GetProject(shopUrl);
            if (project != null)
            {
                return new HttpResponseMessage() { Content = new StringContent("{\"status\": \"ok\"}") };
            }
            else
            {
                return new HttpResponseMessage() { Content = new StringContent("{\"status\": \"fail\"}") };
            }
        }
    }
}
