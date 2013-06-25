using NUnit.Framework;

namespace RepoLibraryTests
{
    [TestFixture]
    public class RepoLibraryTests
    {
        [Test]
        public void ShouldReturnOlolo()
        {
            var sut = new RepoLibrary.RepoLibrary();
            var str = sut.ToString();
            Assert.That(str, Is.EqualTo("ololo"));
        }

    }
}
