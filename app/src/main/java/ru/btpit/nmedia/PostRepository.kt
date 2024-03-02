package ru.btpit.nmedia
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id:Long)
    fun shareById(id:Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun edit(post: Post)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            header = "ISQ",
            content = "бесплатная система мгновенного обмена текстовыми сообщениями для мобильных и иных платформ с поддержкой голосовой и видеосвязи. Позволяет пересылать текстовые сообщения, изображения, видео и аудио через Интернет.",
            dataTime = "21 февраля в 19:12",
            isLike = false,
            amountlike = 999,
            amountrepost = 15,
            amountview = 500,
            isRepos = false
        ),
        Post(
            id = nextId++,
            header = "ISQ",
            content = " бесплатная система мгновенного обмена текстовыми сообщениями для мобильных и иных платформ с поддержкой голосовой и видеосвязи. Позволяет пересылать текстовые сообщения, изображения, видео и аудио через Интернет.",
            dataTime = "27 Февраля в 12:56",
            isLike = false,
            amountlike = 0,
            amountrepost = 0,
            amountview = 0,
            isRepos = false
        ),

        )

    private val data = MutableLiveData(posts)
    private val edited = MutableLiveData(empty)
    override fun get(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (it.isLike)
                    it.amountlike--
                else
                    it.amountlike++
                it.copy(isLike = !it.isLike)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.amountrepost++
                it.copy(isRepos = !it.isRepos)
            }
        }
        data.value = posts
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id}
        data.value = posts
    }
    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    header = "Me",
                    isLike = false,
                    isRepos = false,
                    dataTime = "now",
                    amountview = 0
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
    override fun edit(post: Post) {
        edited.value = post
    }
}
private val empty = Post(
    id = 0,
    content = "",
    amountview = 0,
    amountlike = 0,
    amountrepost = 0,
    dataTime = "",
    header = "",
    isLike = false,
    isRepos = false
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    val edited = MutableLiveData(empty)
    fun edit(post: Post) {
        edited.value = post
    }
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id:Long) = repository.shareById(id)
    fun removeById(id : Long) = repository.removeById(id)
}