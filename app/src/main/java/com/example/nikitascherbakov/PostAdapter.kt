package ru.btpit.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nikitascherbakov.R
import com.example.nikitascherbakov.databinding.PostCardBinding


interface onInteractionListener {
    fun onLike(post : Post) {}
    fun onEdit(post : Post) {}
    fun onRemove(post : Post) {}
    fun onShare(post:Post) {}
}
class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: onInteractionListener
)  : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            textViewHeader.text = post.header
            textViewContent.text = post.content
            textViewDateTime.text = post.dataTime
            textViewLike.text = numberRangeSwitch(post.amountlike)
            textViewRepost.text = numberRangeSwitch(post.amountrepost)
            imagebutnlike.setBackgroundResource(
                if (post.isLike)
                    R.drawable.like_unpress
                else
                    R.drawable.like_press
            )
            imagebutnlike.setOnClickListener{
                onInteractionListener.onLike(post)
            }
            imagebutnRepost.setOnClickListener{
                onInteractionListener.onShare(post)
            }
            imageViewmenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.removes -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.editing -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostsAdapter(
    private val onInteractionListener: onInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding,
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

private fun numberRangeSwitch(value: Int): String { // Switches display based on the size of the number
    val v1: String = value.toString()
    return if (value > 999)
        when(value) {
            in 1000..1099  -> v1[0].toString() + "K"
            in 1100 .. 9999 -> v1[0].toString() + "." + v1[1].toString() + "K"
            in 10000 .. 99999 -> v1[0].toString() + v1[1].toString() + "K"
            in 100_000 .. 999_999 -> v1[0].toString() + v1[1].toString() + v1[2].toString() + "K"
            in 1_000_000 .. 999_999_999 -> v1[0].toString() + "." + v1[1].toString() + "M"
            else -> "ꚙ"
        } else value.toString()
}
