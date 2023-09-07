package hu.paulolajos.trackerlocation.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import hu.paulolajos.trackerlocation.databinding.FragmentLoginBinding
import hu.paulolajos.trackerlocation.ui.maps.MapsFragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context: Context = requireActivity()

        binding.btnLogin.setOnClickListener {

            if (isFormValid()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()

                ).addOnSuccessListener {
                    binding.btnLogin.isEnabled = false

                    //startActivity(Intent(this@MainActivity, ForumActivity::class.java))
                    /*
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(binding.root.id, MapsFragment.newInstance())
                        .commitNow()

                     */

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(
                            (requireView().parent as ViewGroup).id,
                            MapsFragment.newInstance()
                        )
                        .addToBackStack(null)
                        .commit()

                    Toast.makeText(
                        context,
                        "Login successful",
                        Toast.LENGTH_LONG
                    ).show()

                }.addOnFailureListener{

                    val errorMessage = "Login error: ${it.message}"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }

        binding.btnRegister.setOnClickListener {

            binding.btnRegister.isEnabled = false

            if (isFormValid()) {

                var errorMessage: String

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()

                ).addOnSuccessListener {
                    errorMessage = "Registration OK"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE

                }.addOnFailureListener{
                    errorMessage = "Error: ${it.message}"

                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    binding.tvError.text = errorMessage
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            binding.etEmail.text.isEmpty() -> {
                binding.etEmail.error = "This field can not be empty"
                false
            }
            binding.etPassword.text.isEmpty() -> {
                binding.etPassword.error = "The password can not be empty"
                false
            }
            else -> true
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}