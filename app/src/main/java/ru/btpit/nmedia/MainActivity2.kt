package ru.btpit.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nikitascherbakov.databinding.ActivityMainBinding
import com.example.nikitascherbakov.databinding.PostCardBinding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val bind: PostCardBinding = PostCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(
            object : onInteractionListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }
            })
        binding.listCool.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.otpravitbtn.setOnClickListener{
            with(binding.editContent) {
                if (text.isNullOrBlank()) {
                    android.widget.Toast.makeText(
                        this@MainActivity2,
                        "Content can't be empty",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                viewModel.save()
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with (binding.editContent) {
                requestFocus()
                setText(post.content)
            }
        }
    }
}