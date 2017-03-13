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
package fr.gouv.vitam.generator.seda.module.invalid;

import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

import fr.gouv.culture.archivesdefrance.seda.v2.BinaryDataObjectTypeRoot;
import fr.gouv.culture.archivesdefrance.seda.v2.FileInfoType;
import fr.gouv.vitam.common.exception.VitamException;
import fr.gouv.vitam.generator.scheduler.api.ParameterMap;
import fr.gouv.vitam.generator.scheduler.api.TaskInfo;
import fr.gouv.vitam.generator.seda.api.SedaModuleParameter;

public class InvalidSizeModuleTest {
    private static final String TESTFILE = "sip1.json";
    private static final long REALSIZE = 1337;
    private static final String FALSESIZE = "10";

    @Test
    public void invalidSizeMatchingFile() {
        ParameterMap pm = new ParameterMap();
        BinaryDataObjectTypeRoot bdotr = new BinaryDataObjectTypeRoot();
        FileInfoType fit = new FileInfoType();
        fit.setFilename(TESTFILE);
        bdotr.setFileInfo(fit);
        bdotr.setSize(BigInteger.valueOf(REALSIZE));
        pm.put("file_regex", "^.*sip.*json");
        pm.put("false_size", FALSESIZE);
        pm.put(SedaModuleParameter.BINARYDATAOBJECT.getName(), bdotr);
        InvalidSizeModule ism = new InvalidSizeModule();
        try {
            TaskInfo taskInfo = ism.execute(pm);
            bdotr = (BinaryDataObjectTypeRoot) taskInfo.getParameterMap().get(SedaModuleParameter.BINARYDATAOBJECT.getName());
            if (!bdotr.getSize().toString().equals(FALSESIZE)) {
                fail("The size has not been changed ");
            }

        } catch (VitamException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void invalidSizeNotMatchingFile() {
        ParameterMap pm = new ParameterMap();
        BinaryDataObjectTypeRoot bdotr = new BinaryDataObjectTypeRoot();
        FileInfoType fit = new FileInfoType();
        fit.setFilename(TESTFILE);
        bdotr.setFileInfo(fit);
        bdotr.setSize(BigInteger.valueOf(REALSIZE));
        pm.put("file_regex", "NOTMATCHING.*json");
        pm.put("false_size", FALSESIZE);
        pm.put(SedaModuleParameter.BINARYDATAOBJECT.getName(), bdotr);
        InvalidSizeModule ism = new InvalidSizeModule();
        try {
            TaskInfo taskInfo = ism.execute(pm);
            bdotr = (BinaryDataObjectTypeRoot) taskInfo.getParameterMap().get(SedaModuleParameter.BINARYDATAOBJECT.getName());
            if (bdotr.getSize().toString().equals(FALSESIZE)) {
                fail("The size has been changed but the file is not matching the regex");
            }

        } catch (VitamException e) {
            fail(e.getMessage());
        }
    }

}
