using System;
using System.Data;
using System.Data.SqlClient;

namespace RepoLibrary
{
    class MSSQLDatabaseGateway : IDatabaseGateway
    {
        public MSSQLDatabaseGateway(string connectionString)
        {
            this.connectionString = connectionString;
        }

        public Project GetProject(int id)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlDataReader myReader = null;
            SqlCommand myCommand = new SqlCommand("select * from projects where projectId=" + id.ToString(), connection);
            myReader = myCommand.ExecuteReader();
            while (myReader.Read())
            {
                Console.WriteLine(myReader["projectId"].ToString());
                Console.WriteLine(myReader["projectName"].ToString());
                Project project = new Project();
                project.Id = int.Parse(myReader["projectId"].ToString());
                project.Name = myReader["projectName"].ToString();
                connection.Close();
                return project;
            }
            connection.Close();
            return null;
        }

        public string SaveProject(Project data)
        {
            SqlConnection connection = new SqlConnection(this.connectionString);
            connection.Open();
            SqlCommand myCommand = new SqlCommand("INSERT INTO projects (projectId, projectName) " +
                "Values (" + data.Id.ToString() + ", '" + data.Name + "')", connection);
            int result = myCommand.ExecuteNonQuery();
            connection.Close();
            if (result > 0)
            {
                return "ok";
            }
            else
            {
                return "Failed to add project";
            }

        }
        private string connectionString;
    }
}