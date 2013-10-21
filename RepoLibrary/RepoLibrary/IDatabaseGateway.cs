namespace RepoLibrary
{
    public interface IDatabaseGateway
    {
        Project GetProject(string url);

        Project GetProjectByKey(string key);

        string SaveProject(Project data);
    }
}
