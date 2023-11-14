import android.content.Intent
import com.example.blinkchat.R
import com.example.blinkchat.models.User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkchat.activities.ChatActivity
import com.example.blinkchat.fragments.EmptyViewHolder
import com.example.blinkchat.fragments.UsersViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val DELETED_VIEW_TYPE = 1
private const val NORMAL_VIEW_TYPE = 2

class PeopleFragment : Fragment() {

    private lateinit var mAdapter: FirestoreRecyclerAdapter<User, UsersViewHolder>
    private val database by lazy {
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name")
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupAdapter()
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    private fun setupAdapter() {
        val query = database
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        mAdapter = object : FirestoreRecyclerAdapter<User, UsersViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return when (viewType) {
                    NORMAL_VIEW_TYPE -> UsersViewHolder(
                        inflater.inflate(
                            R.layout.list_item,
                            parent,
                            false
                        )
                    )

                    else -> EmptyViewHolder(inflater.inflate(R.layout.empty_view, parent, false))
                }
            }


            override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, user: User) {
                if (viewHolder is UsersViewHolder)
                    if (auth.uid == user.uid) {
//                        currentList?.snapshot()?.removeAt(position)
//                        notifyItemRemoved(position)
                    } else
                        viewHolder.bind(user) { name: String, photo: String, id: String ->
                            startActivity(
                                ChatActivity.createChatActivity(
                                    requireContext(),
                                    id,
                                    name,
                                    photo
                                )
                            )
                        }
            }

//            override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, user: User) {
//                if (auth.uid != user.uid) {
//                    viewHolder.bind(user) { name: String, photo: String, id: String ->
//                        val intent = Intent(requireContext(), ChatActivity::class.java)
//                        intent.putExtra("userId", id)
//                        intent.putExtra("userName", name)
//                        intent.putExtra("userPhoto", photo)
//                        startActivity(intent)
//                    }
//                }
//            }


            override fun getItemViewType(position: Int): Int {
                val item = getItem(position)
                if (auth.currentUser?.uid == item.uid) {
                    return DELETED_VIEW_TYPE
                }
                return NORMAL_VIEW_TYPE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }
}