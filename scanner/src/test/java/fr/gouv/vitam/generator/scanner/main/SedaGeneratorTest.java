/**
 * Copyright French Prime minister Office/SGMAP/DINSIC/Vitam Program (2015-2019)
 *
 * contact.vitam@culture.gouv.fr
 * 
 * This software is a computer program whose purpose is to implement a digital 
 * archiving back-office system managing high volumetry securely and efficiently.
 *
 * This software is governed by the CeCILL 2.1 license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL 2.1
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL 2.1 license and that you accept its terms.
 */
package fr.gouv.vitam.generator.scanner.main;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import fr.gouv.vitam.common.exception.VitamException;

public class SedaGeneratorTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void TestScanNominalCase() {
        ClassLoader classLoader = getClass().getClassLoader();
        String workingdir = classLoader.getResource(".").getFile();
        String scandir = classLoader.getResource("sip1").getFile();
        String[] args = {workingdir, scandir};
        try {
            SedaGenerator.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void Test1Argument() {
        String[] args = {"only1argument"};

        try {
            exit.expectSystemExitWithStatus(1);
            SedaGenerator.main(args);
        } catch (VitamException | IOException | XMLStreamException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void TestScanDirIsAFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        String workingdir = classLoader.getResource(".").getFile();
        String scandir = classLoader.getResource("playbook_binary.json").getFile();
        String[] args = {workingdir, scandir};
        try {
            exit.expectSystemExitWithStatus(2);
            SedaGenerator.main(args);
        } catch (VitamException | IOException | XMLStreamException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void TestMissingFieldInConfigFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        String workingdir = classLoader.getResource("bad_conf").getFile();
        String scandir = classLoader.getResource("sip1").getFile();
        String[] args = {workingdir, scandir};
        try {
            exit.expectSystemExitWithStatus(3);
            SedaGenerator.main(args);
        } catch (VitamException | IOException | XMLStreamException e) {
            e.printStackTrace();
            fail();
        }
    }
}
