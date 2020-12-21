package java.com.somanath.example.contentplayerdemo.home.view

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.somanath.example.contentplayerdemo.R


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PlayerFragment : Fragment() , Player.EventListener{

    var fullscreen = false
    companion object{
        const val TAG = "PlayerFragment"
    }
    private  var simpleExoplayer: SimpleExoPlayer? = null
    private  lateinit var progressBar: ProgressBar
    private var playbackPosition: Long = 0
    private lateinit var contentUrl : String
    private lateinit var exoplayerView: PlayerView
    private var playWhenReady = true
    private var currentWindow = 0
    private var fullscreenButton: ImageView? = null
    private lateinit var  videoQualityChanger : ImageView
    private lateinit var pipSwitch: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exoplayerView = view.findViewById<PlayerView>(R.id.exoplayerView)
        progressBar = view.findViewById(R.id.progressBar)
        videoQualityChanger = view.findViewById(R.id.exo_track_selection_view)
        pipSwitch = view.findViewById(R.id.pipSwitch)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        initFullScreenButton()

        videoQualityChanger.setOnClickListener { showQualityPopup() }
        pipSwitch.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
                val aspectRatio = Rational(200, 200)
                mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio)
                activity?.enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    activity?.enterPictureInPictureMode()
                }
            }
        }
    }

    fun initFullScreenButton(){
        fullscreenButton = exoplayerView.findViewById(R.id.exo_fullscreen_icon)
        fullscreenButton!!.setOnClickListener {
            if (fullscreen) {
                fullscreenButton!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_fullscreen_open
                    )
                )

                if (activity?.actionBar != null) {
                    activity?.actionBar!!.show()
                }
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val params = exoplayerView.getLayoutParams() as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (200 * getResources().getDisplayMetrics().density).toInt()
                exoplayerView.setLayoutParams(params)
                fullscreen = false
            } else {
                fullscreenButton!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_fullscreen_close
                    )
                )
                if (activity?.actionBar != null) {
                    activity?.actionBar!!.hide()
                }
                activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                val params = exoplayerView.getLayoutParams() as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                exoplayerView.setLayoutParams(params)
                fullscreen = true
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || simpleExoplayer == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    private fun initializePlayer() {

        contentUrl = activity?.intent?.getStringExtra("content_url")!!

        if (simpleExoplayer == null) {

            val trackSelector = DefaultTrackSelector(requireContext())
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            trackSelector.experimentalAllowMultipleAdaptiveSelections()

            simpleExoplayer = SimpleExoPlayer.Builder(requireActivity())
                .setTrackSelector(trackSelector)
                .build()
            simpleExoplayer?.addListener(this)
            simpleExoplayer?.prepare()
            exoplayerView.setPlayer(simpleExoplayer)
        val mediaItem = MediaItem.Builder()
            .setUri(contentUrl)
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()
        val userAgent = Util.getUserAgent(
            requireContext(),
            requireActivity().getString(R.string.app_name)
        )
        val mediaSource = ExtractorMediaSource
            .Factory(DefaultDataSourceFactory(requireContext(), userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(contentUrl))
       // simpleExoplayer?.addMediaItem(mediaItem)
        simpleExoplayer?.prepare(mediaSource)
        simpleExoplayer?.setPlayWhenReady(playWhenReady)
        simpleExoplayer?.seekTo(currentWindow, playbackPosition)
            showQualityPopup()
        //simpleExoplayer?.prepare()
    }

//    private fun initializePlayer() {
//        contentUrl = arguments?.getString("content_url")!!
//        simpleExoplayer = SimpleExoPlayer.Builder(requireContext()).build()
//        preparePlayer(contentUrl, "dash")
//        exoplayerView.player = simpleExoplayer
//        simpleExoplayer!!.seekTo(playbackPosition)
//        simpleExoplayer!!.playWhenReady = true
//        simpleExoplayer!!.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
      //  val mediaSource = buildMediaSource(uri, type)
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()
        simpleExoplayer!!.setMediaItem(mediaItem)
        simpleExoplayer!!.prepare()
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        // handle error
        Log.d(TAG, error.toString())
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
         progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            progressBar.visibility = View.INVISIBLE
    }


    private fun releasePlayer() {
        if (simpleExoplayer != null) {
            playWhenReady = simpleExoplayer!!.playWhenReady
            playbackPosition = simpleExoplayer!!.currentPosition
            currentWindow = simpleExoplayer!!.currentWindowIndex
            simpleExoplayer!!.removeListener(this)
            simpleExoplayer!!.release()
            simpleExoplayer = null
        }
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
        when(state){
            Player.STATE_READY -> {
                Log.d(TAG, "STATE_READY")
                videoQualityChanger.isEnabled = true
            }
            Player.STATE_BUFFERING -> Log.d(TAG, "STATE_BUFFERING")
            Player.STATE_ENDED -> Log.d(TAG, "STATE_ENDED")
            Player.STATE_IDLE -> videoQualityChanger.isEnabled = false
        }
    }

    private fun showQualityPopup(){
        val trackSelector = simpleExoplayer?.trackSelector as DefaultTrackSelector
        val mappedTrackInfo: MappedTrackInfo? = trackSelector.currentMappedTrackInfo
        if (mappedTrackInfo != null) {
            val title: CharSequence = "Select Resolution "
            val rendererIndex = 0 // renderer for video
            val rendererType = mappedTrackInfo.getRendererType(rendererIndex)
            val allowAdaptiveSelections = (rendererType == C.TRACK_TYPE_VIDEO
                    || (rendererType == C.TRACK_TYPE_AUDIO
                    && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                    == MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS))
            val build = TrackSelectionDialogBuilder(
                requireActivity(),
                title,
                trackSelector,
                rendererIndex
            )
            build.setAllowAdaptiveSelections(allowAdaptiveSelections)
            build.build().show()
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        if(isInPictureInPictureMode){

        } else{

        }
    }
}