package com.example.surabhi.helloar_edited;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();


            ViewRenderable.builder().setView(this, R.layout.tv).build().thenAccept(
                    (renderable) -> {
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        TransformableNode infoCard = new TransformableNode(arFragment.getTransformationSystem());
                        infoCard.setParent(anchorNode);
                        infoCard.setLocalPosition(new Vector3(0f, 0.01f, 0f));
                        infoCard.setRenderable(renderable);

                        renderable.setShadowCaster(false);
                        renderable.setShadowReceiver(false);

                        float[] yAxis = plane.getCenterPose().getYAxis();
                        Vector3 planeNormal = new Vector3(yAxis[0], yAxis[1], yAxis[2]);
                        Quaternion upQuat = Quaternion.lookRotation(planeNormal, Vector3.up());
                        infoCard.setWorldRotation(upQuat.inverted());



                        TextView textView = (TextView) renderable.getView();
                        textView.setText("decoded sentence\nthis is a long one\nwill go all the way");
                        arFragment.getArSceneView().getScene().addChild(anchorNode);
                        infoCard.select();
                    }
            );

//            ModelRenderable.builder().setSource(this, Uri.parse("orgchart_rec_white.sfb")).build()
//                    .thenAccept(modelRenderable -> addModelToScene(anchor, modelRenderable))
//                    .exceptionally(throwable -> {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage(throwable.getMessage()).show();
//                        return null;
//                    });
        }));

    }

}
