package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Essi Supponen on 16/03/2018.
 */

// Poistetaan ku Debugia ei enää tarvita

public class FaceDebug {
    private char faceShapeImg;
    private char hairImg;
    private char eyesImg;
    private char noseImg;
    private char mouthImg;
    private char accessoryImg;

    private String idCode;
    private int[] featureIds;

    public FaceDebug(char[] faceShapes, char[] hairs, char[] eyes, char[] noses, char[] mouths) {

        int randomFaceShape = MathUtils.random(0, faceShapes.length - 1);
        int randomHair = MathUtils.random(0, hairs.length - 1);
        int randomEyes = MathUtils.random(0, eyes.length - 1);
        int randomNose = MathUtils.random(0, noses.length - 1);
        int randomMouth = MathUtils.random(0, mouths.length - 1);

        idCode = randomFaceShape + "-" + randomHair + "-" + randomEyes + "-" + randomNose +
                "-" + randomMouth;

        faceShapeImg = faceShapes[randomFaceShape];
        hairImg = hairs[randomHair];
        eyesImg = eyes[randomEyes];
        noseImg = noses[randomNose];
        mouthImg = mouths[randomMouth];

        featureIds = new int[5];
        featureIds[0] = randomFaceShape;
        featureIds[1] = randomHair;
        featureIds[2] = randomEyes;
        featureIds[3] = randomNose;
        featureIds[4] = randomMouth;
    }

    public FaceDebug(char[] faceShapes, char[] hairs, char[] eyes, char[] noses, char[] mouths,
                   int[] attributes, boolean useSameAttributes) {
        int faceShapeId = 0;
        int hairId = 0;
        int eyesId = 0;
        int noseId = 0;
        int mouthId = 0;

        if (useSameAttributes) {
            if (attributes[0] == -1) {
                faceShapeId = MathUtils.random(0, faceShapes.length - 1);
            } else {
                faceShapeId = attributes[0];
            }

            if (attributes[1] == -1) {
                hairId = MathUtils.random(0, hairs.length - 1);
            } else {
                hairId = attributes[1];
            }

            if (attributes[2] == -1) {
                eyesId = MathUtils.random(0, eyes.length - 1);
            } else {
                eyesId = attributes[2];
            }

            if (attributes[3] == -1) {
                noseId = MathUtils.random(0, noses.length - 1);
            } else {
                noseId = attributes[3];
            }

            if (attributes[4] == -1) {
                mouthId = MathUtils.random(0, mouths.length - 1);
            } else {
                mouthId = attributes[4];
            }
        } else {
            boolean attributeSet = false;
            int index;

            while (!attributeSet) {
                index = MathUtils.random(0, faceShapes.length-1);

                if (index != attributes[0]) {
                    faceShapeId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, hairs.length-1);

                if (index != attributes[1]) {
                    hairId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, eyes.length-1);

                if (index != attributes[2]) {
                    eyesId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, noses.length-1);

                if (index != attributes[3]) {
                    noseId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, mouths.length-1);

                if (index != attributes[4]) {
                    mouthId = index;
                    attributeSet = true;
                }
            }
        }

        idCode = faceShapeId + "-" + hairId + "-" + eyesId + "-" + noseId + "-" + mouthId;

        faceShapeImg = faceShapes[faceShapeId];
        hairImg = hairs[hairId];
        eyesImg = eyes[eyesId];
        noseImg = noses[noseId];
        mouthImg = mouths[mouthId];

        featureIds = new int[5];
        featureIds[0] = faceShapeId;
        featureIds[1] = hairId;
        featureIds[2] = eyesId;
        featureIds[3] = noseId;
        featureIds[4] = mouthId;
    }

    public int[] getFeatureIds() {
        return featureIds;
    }

    public String getIdCode() {
        return idCode;
    }

    public void draw() {
        System.out.println(faceShapeImg + " + " + hairImg + " + " + eyesImg + " + " + noseImg
                + " + " + mouthImg);
    }
}
