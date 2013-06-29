using NUnit.Framework;
using Moq;
using RepoLibrary;

namespace RepoLibraryTests
{
    [TestFixture]
    public class RepoLibraryTests
    {

        [Test]
        public void ShouldReturnOlolo()
        {
            var fakeDb = new Mock<IDatabaseGateway>();

            var sut = new RepoLibrary.RepoLibrary(fakeDb.Object);
            var str = sut.ToString();

            Assert.That(str, Is.EqualTo("ololo"));
        }

        [Test]
        public void ShouldSaveProject()
        {
            var project = new Project() { Id = 1, Name = "test" };
            var fakeDb = new Mock<IDatabaseGateway>();

            var sut = new RepoLibrary.RepoLibrary(fakeDb.Object);
            sut.SaveProject(project);

            fakeDb.Verify(x => x.SaveProject(project), Times.Once());
        }

        [Test]
        public void ShouldSaveProjectOk()
        {
            var project = new Project() { Id = 1, Name = "test" };
            var fakeDb = new Mock<IDatabaseGateway>();
            fakeDb.Setup(x => x.SaveProject(project)).Returns("ok");

            var sut = new RepoLibrary.RepoLibrary(fakeDb.Object);
            var str = sut.SaveProject(project);

            Assert.That(str, Is.EqualTo("ok"));
        }

    }
}
