package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Essi Supponen on 16/03/2018.
 */

// HUOM! Tämä luokka ei toimi vielä tämän pelin attribuuteilla. Muokkaan sen toimivaksi, kunhan
// alustavat grafiikat saadaan

public class RowConstructorTest {
    private int rowLength;          // 3 - 6 people
    private int sameFeatures;       // 0 - 4 same features
    private boolean accessories;

    private boolean sameShape;
    private boolean sameHair;
    private boolean sameEyes;
    private boolean sameNose;
    private boolean sameMouth;

    char[] faceShapeTextures;
    char[] hairTextures;
    char[] eyesTextures;
    char[] noseTextures;
    char[] mouthTextures;

    public RowConstructorTest() {
        faceShapeTextures = new char[5];
        hairTextures = new char[10];
        eyesTextures = new char[10];
        noseTextures = new char[10];
        mouthTextures = new char[10];

        setArrayValues();
    }

    private void setArrayValues() {
        faceShapeTextures[0] = 'A';
        faceShapeTextures[1] = 'B';
        faceShapeTextures[2] = 'C';
        faceShapeTextures[3] = 'D';
        faceShapeTextures[4] = 'E';

        hairTextures[0] = 'A';
        hairTextures[1] = 'B';
        hairTextures[2] = 'C';
        hairTextures[3] = 'D';
        hairTextures[4] = 'E';
        hairTextures[5] = 'F';
        hairTextures[6] = 'G';
        hairTextures[7] = 'H';
        hairTextures[8] = 'I';
        hairTextures[9] = 'J';

        eyesTextures[0] = 'A';
        eyesTextures[1] = 'B';
        eyesTextures[2] = 'C';
        eyesTextures[3] = 'D';
        eyesTextures[4] = 'E';
        eyesTextures[5] = 'F';
        eyesTextures[6] = 'G';
        eyesTextures[7] = 'H';
        eyesTextures[8] = 'I';
        eyesTextures[9] = 'J';

        noseTextures[0] = 'A';
        noseTextures[1] = 'B';
        noseTextures[2] = 'C';
        noseTextures[3] = 'D';
        noseTextures[4] = 'E';
        noseTextures[5] = 'F';
        noseTextures[6] = 'G';
        noseTextures[7] = 'H';
        noseTextures[8] = 'I';
        noseTextures[9] = 'J';

        mouthTextures[0] = 'A';
        mouthTextures[1] = 'B';
        mouthTextures[2] = 'C';
        mouthTextures[3] = 'D';
        mouthTextures[4] = 'E';
        mouthTextures[5] = 'F';
        mouthTextures[6] = 'G';
        mouthTextures[7] = 'H';
        mouthTextures[8] = 'I';
        mouthTextures[9] = 'J';
    }

    public FaceDebug[] makeRowDifficulty(int difficultyDegree) {
        return new FaceDebug[1];
    }

    public FaceDebug[] makeRow(int rowLength, int sameFeatures, boolean accessories) {
        this.rowLength = rowLength;
        this.sameFeatures = sameFeatures;
        this.accessories = accessories;

        setSame();

        FaceDebug[] row = generateTheFaces();

        return row;
    }

    private void setSame() {
        boolean[] sameArray = new boolean[5];

        for (boolean value : sameArray) {
            value = false;
        }

        if (sameFeatures >= 1 && sameFeatures <= 3) {
            int sameToBeSet = sameFeatures;
            int randomPoint = -1;

            while (sameToBeSet > 0) {
                randomPoint = MathUtils.random(0, 4);

                if (sameArray[randomPoint] == false) {
                    sameArray[randomPoint] = true;
                    sameToBeSet--;
                }
            }
        } else if (sameFeatures == 4) {
            int randomPoint = MathUtils.random(1, 4);

            for (int i = 0; i <= 4; i++) {
                if (i != randomPoint) {
                    sameArray[i] = true;
                }
            }
        }

        sameShape = sameArray[0];
        sameHair = sameArray[1];
        sameEyes = sameArray[2];
        sameNose = sameArray[3];
        sameMouth = sameArray[4];
    }

    private FaceDebug[] generateTheFaces() {
        FaceDebug[] row = new FaceDebug[rowLength];

        row[0] = new FaceDebug(faceShapeTextures, hairTextures, eyesTextures,
                noseTextures, mouthTextures);
        int[] attributes = row[0].getFeatureIds();

        if (!sameShape && !sameHair && !sameEyes && !sameNose && !sameMouth) {
            row = addCriminalsToRow(row, attributes,false);
        } else {
            if (!sameShape) {
                attributes[0] = -1;
            }
            if (!sameHair) {
                attributes[1] = -1;
            }
            if (!sameEyes) {
                attributes[2] = -1;
            }
            if (!sameNose) {
                attributes[3] = -1;
            }
            if (!sameMouth) {
                attributes[4] = -1;
            }

            row = addCriminalsToRow(row, attributes,true);
        }

        return row;
    }

    private FaceDebug[] addCriminalsToRow(FaceDebug[] row, int[] attributes,
                                        boolean useSameAttributes) {
        int criminalsToAdd = row.length - 1;

        while (criminalsToAdd > 0) {
            FaceDebug criminal;
            boolean sameFound = false;

            criminal = new FaceDebug(faceShapeTextures, hairTextures, eyesTextures,
                    noseTextures, mouthTextures, attributes, useSameAttributes);

            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    if (row[i].getIdCode().equals(criminal.getIdCode())) {
                        sameFound = true;
                    }
                }
            }

            if (!sameFound) {
                row[row.length - criminalsToAdd] = criminal;
                criminalsToAdd--;
            }
        }

        return row;
    }
}
