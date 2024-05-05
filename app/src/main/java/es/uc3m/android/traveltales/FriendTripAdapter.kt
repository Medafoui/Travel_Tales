package es.uc3m.android.traveltales

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class FriendTripAdapter(var tripList: List<FriendTrip>) : RecyclerView.Adapter<FriendTripAdapter.FriendTripViewHolder>() {

    class FriendTripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.findViewById(R.id.friend_name)
        val friendTrip: TextView = itemView.findViewById(R.id.friend_trip)
        val tripImageView: ImageView = itemView.findViewById(R.id.trip_background)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendTripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friends_trip, parent, false)
        return FriendTripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendTripViewHolder, position: Int) {
        val currentItem = tripList[position]
        holder.friendName.text = currentItem.username
        holder.friendTrip.text = currentItem.tripName

        Glide.with(holder.itemView.context)
            .load(currentItem.tripImageUri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("TripAdapter", "Load failed", e)
                    return false // let Glide handle the rest
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                    return false // let Glide handle the rest
                }
            })
            .into(holder.tripImageView)
    }

    override fun getItemCount() = tripList.size
}

