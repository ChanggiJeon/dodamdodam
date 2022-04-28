package com.ssafy.family.ui.main.bottomFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.ssafy.family.config.ApplicationClass.Companion.Id
import com.ssafy.family.config.ApplicationClass.Companion.Name
import com.ssafy.family.data.ChatData
import com.ssafy.family.databinding.FragmentChattingBinding
import com.ssafy.family.ui.Adapter.ChattingAdapter
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding

    var database: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    val datas = mutableListOf<ChatData>()
    val familyCode = "fam_code"
    lateinit var chattingAdapter:ChattingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        myRef = database!!.getReference("message" + familyCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var context = requireContext()
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(ChatData::class.java)
                datas.add(data!!)
                chattingAdapter = ChattingAdapter(context, datas)
                binding.chattingRecyclerView.adapter = chattingAdapter
                binding.chattingRecyclerView.smoothScrollToPosition(chattingAdapter.itemCount)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        myRef!!.addChildEventListener(childEventListener)

        binding.submitButton.setOnClickListener(View.OnClickListener {
            val message: String = binding.chattingText.text.toString()
            if (message != "") {
                val now: LocalDateTime = LocalDateTime.now()
                val nowTime = now.format(DateTimeFormatter.ofPattern("a h시 mm분").withLocale(Locale.forLanguageTag("ko")))
                val data = ChatData(Id, Name, message, nowTime)
                myRef!!.push().setValue(data)
                binding.chattingText.setText("")

            }
        })
    }
}