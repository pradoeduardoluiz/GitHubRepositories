package br.com.prado.eduardo.luiz.githubrepositories.mappers

import androidx.paging.PagingData
import br.com.prado.eduardo.luiz.githubrepositories.collectDataForTest
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.OwnerModel
import br.com.prado.eduardo.luiz.githubrepositories.domain.model.RepositoryModel
import br.com.prado.eduardo.luiz.githubrepositories.ui.repositories.RepositoriesState
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class RepositoriesMapperTest {

  @Test
  fun `should map paging data model to paging data state`() = runBlocking {

    val expected = listOf(
      RepositoriesState.Item(
        id = 1,
        name = "name",
        fullName = "fullName",
        isPrivate = false,
        ownerName = "name",
        ownerImageUrl = "avatarUrl",
        description = "description",
        url = "url",
        forks = 10,
        stars = 1
      )
    )

    val models = listOf(
      RepositoryModel(
        id = 1,
        name = "name",
        fullName = "fullName",
        isPrivate = false,
        owner = OwnerModel(
          name = "name",
          avatarUrl = "avatarUrl"
        ),
        description = "description",
        url = "url",
        forks = 10,
        stars = 1
      )
    )

    val pagingDataModel = PagingData.from(models)
    val pagingData = pagingDataModel.mapToState()

    val actual = pagingData.collectDataForTest()
    Assert.assertEquals(expected, actual)
  }

  @Test
  fun `should map paging repository model model to repository state`() = runBlocking {

    val expected = RepositoriesState.Item(
      id = 1,
      name = "name",
      fullName = "fullName",
      isPrivate = false,
      ownerName = "name",
      ownerImageUrl = "avatarUrl",
      description = "description",
      url = "url",
      forks = 10,
      stars = 1
    )

    val model = RepositoryModel(
      id = 1,
      name = "name",
      fullName = "fullName",
      isPrivate = false,
      owner = OwnerModel(
        name = "name",
        avatarUrl = "avatarUrl"
      ),
      description = "description",
      url = "url",
      forks = 10,
      stars = 1
    )

    val actual = model.mapToState()
    Assert.assertEquals(expected, actual)
  }

}
