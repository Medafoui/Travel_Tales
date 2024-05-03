package es.uc3m.android.traveltales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeopleAdapter(var peopleList: List<Person>, private val listener: OnPersonClickListener ) : RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    interface OnPersonClickListener {
        fun onPersonClick(position: Int)
    }
    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val peopleName: TextView = itemView.findViewById(R.id.friend_name)
        val peoplePicture: ImageView = itemView.findViewById(R.id.friend_profile_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.people_row, parent, false)
        return PeopleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val currentItem = peopleList[position]
        holder.peopleName.text = currentItem.username

        holder.itemView.setOnClickListener {
            listener.onPersonClick(position)
        }
    }

    override fun getItemCount() = peopleList.size

}


