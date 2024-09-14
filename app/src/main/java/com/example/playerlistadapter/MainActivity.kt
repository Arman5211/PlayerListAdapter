package com.example.playerlistadapter


import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playerlistadapter.adapter.PlayerAdapter
import com.example.playerlistadapter.model.Player

class MainActivity : AppCompatActivity() {
    private lateinit var playerlist: MutableList<Player>
    private lateinit var recyclerView: RecyclerView
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editPlayereditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("players", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerView)
        editPlayereditText = findViewById(R.id.editEt)
        playerlist = retrivePlayers()


        val saveButton: Button = findViewById(R.id.saveBtn)

        saveButton.setOnClickListener {
            val playerText = editPlayereditText.text.toString()
            if (playerText.isNotEmpty()) {
                val player = Player(playerText, 10, "right")
                playerlist.add(player)
                savePlayers(playerlist)
                playerAdapter.notifyItemInserted(playerlist.size - 1)
                editPlayereditText.text.clear()
            } else {
                Toast.makeText(this, "player tittle can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        playerAdapter = PlayerAdapter(playerlist, object : PlayerAdapter.PlayerClickListen {
            override fun onEditClick(position: Int) {
                editPlayereditText.setText(playerlist[position].PlayerPosition)
                playerAdapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Player")
                alertDialog.setMessage("Are you sure you want to delete this player?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deletePlayers(position)
                }
                alertDialog.setNegativeButton("No") { _, _ -> }
                alertDialog.show()
            }


        })
        recyclerView.adapter = playerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun savePlayers(playerlist: MutableList<Player>) {

        val editor = sharedPreferences.edit()

        val playerNames = HashSet<String>()
        playerlist.forEach { playerNames.add(it.PlayerName) }
        editor.putStringSet("PlayerNames", playerNames)

        val playerAges = HashSet<String>()
        playerlist.forEach { playerAges.add(it.PlayerAge.toString()) }
        editor.putStringSet("PlayerAges", playerAges)

        val playerPositions = HashSet<String>()
        playerlist.forEach { playerPositions.add(it.PlayerPosition) }
        editor.putStringSet("PlayerPositions", playerPositions)

        editor.apply()

    }

    private fun retrivePlayers(): MutableList<Player> {
        val tasks = sharedPreferences.getStringSet("tasks", HashSet())?:HashSet()
        return tasks.map{Player(it, 10,"RIGHT")}.toMutableList()

    }

    private fun deletePlayers(position: Int) {
        playerlist.removeAt(position)
        playerAdapter.notifyItemRemoved(position)
        savePlayers(playerlist)

    }



}

