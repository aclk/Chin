package murata.ss.ss.bsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import murata.co.psc.messaging.vo.MessageVO;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;


public class MqTracer extends MethodTracer {

    private static final String MQ_OUT_FILE_SYFIX = ".txt";

    protected String mqOutfilePrefix = "mqTestSrc";
    protected int messageSendCallCount = 0;
    protected List<String> excludeFields = new ArrayList<String>();

    /**
     * {@inheritDoc}
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // ���s�O�ɁuMQ�Ȃ�v�̓��ꏈ������B
        if (invocation.getThis().getClass().getSimpleName().indexOf("MessagingPSC") != -1
            && invocation.getMethod().getName().equals("send")) {
            outMqSourceTemplate(invocation);
        }
        // �e���\�b�h���ĂԁB
        return super.invoke(invocation);
    }

    private void outMqSourceTemplate(MethodInvocation invocation) {

        try {

            StringBuilder sb = new StringBuilder();

            // ���O���X�g����A���O�L�[�}�b�v���쐬
            HashMap<String, String> excludeFieldMap = new HashMap<String, String>();
            for (String baseValue : excludeFields) {
                String value = baseValue.toLowerCase();
                excludeFieldMap.put(value, value);
            }

            // �����̃��b�Z�[�W�I�u�W�F�N�g���擾()
            MessageVO messageVO = (MessageVO)invocation.getArguments()[0];

            // ����Ȃ�A�����̊m�F������
            if (messageSendCallCount == 0) {
                sb.append("        // MQ���b�Z�[�W���M(���ʊm�F)");
                sb.append(CR);
                sb.append("        List<MessageVO> messages = mockMessagingPSC.getMessages();");
                sb.append(CR);
                sb.append("        // �����m�F");
                sb.append(CR);
                sb.append("        assertEquals(1, messages.size());");
                sb.append(CR);
            }

            sb.append("        // ���b�Z�[�WVO�m�F");
            sb.append(CR);
            sb.append("        ");
            if (messageSendCallCount == 0) {
                sb.append("MessageVO ");
            }
            sb.append("messageVO = messages.get(");
            sb.append(messageSendCallCount);
            sb.append(");");
            sb.append(CR);

            // messageVo���g�̊m�F
            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getTargetTaskName());
            sb.append(", messageVO.getTargetTaskName());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getTargetProgramName());
            sb.append(", messageVO.getTargetProgramName());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getTaskName());
            sb.append(", messageVO.getTaskName());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getProgramName());
            sb.append(", messageVO.getProgramName());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getTargetId());
            sb.append(", messageVO.getTargetId());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getUserId());
            sb.append(", messageVO.getUserId());");
            sb.append(CR);

            sb.append("        assertEquals(");
            writeStrValue(sb , messageVO.getBlockNo());
            sb.append(", messageVO.getBlockNo());");
            sb.append(CR);

            sb.append("        // ���MVO�f�[�^�m�F");
            sb.append(CR);

            // data�v���p�e�B�̊m�F�\�[�X����
            Object data = messageVO.getData();

            String sendDataClassName = data.getClass().getSimpleName();
            sb.append("        ");
            sb.append(sendDataClassName);
            sb.append(" ");
            String paramName = "data";
            if (messageSendCallCount > 0) {
                paramName += (messageSendCallCount+1);
            }
            sb.append(paramName);
            sb.append(" = (");
            sb.append(sendDataClassName);
            sb.append(")messageVO.getData();");
            sb.append(CR);

            Set<String> keys = PropertyUtils.describe(data).keySet();
            for (String key : keys) {
                // �ΏۊO�t�B�[���h����
                if (key.equals("class")) continue;
                if (excludeFieldMap.containsKey(key.toLowerCase())) continue;

                sb.append("        assertEquals(");
                // �v���p�e�B�̏����擾
                Object value = PropertyUtils.getProperty(data, key);
                writeStrValue(sb , value);
                sb.append(", ");
                sb.append(paramName);
                sb.append(".get");
                String methodName = key.substring(0, 1).toUpperCase() + key.substring(1);
                sb.append(methodName);
                sb.append("());");
                sb.append(CR);
            }

            // �t�@�C���o��
            mqOutLog(sb.toString());

            messageSendCallCount++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Object(�����^��String or ���b�p�[�^�̂݊���)�̒l��StringBuilder�ɏ����o���B
     * null�̏ꍇ�́A"null"�Əo�͂���B
     * @param sb
     * @param value
     */
    private void writeStrValue(StringBuilder sb , Object value) {
        if (value == null) {
            sb.append("null");
        } else {
            if (value instanceof Long) {
                sb.append("new Long(");
                sb.append(value.toString());
                sb.append(")");
            } else if (value instanceof BigDecimal) {
                sb.append("new BigDecimal(");
                sb.append(value.toString());
                sb.append(")");
            } else if (value instanceof java.util.Date) {
                Date dateValue = (Date)value;
                sb.append("new Date(");
                sb.append(dateValue.getTime());
                sb.append("L)");
            } else {
                sb.append("\"");
                sb.append(value.toString());
                sb.append("\"");
            }
        }
    }

    /**
     * �o�͏����B
     * �؂�ւ�����悤�Ƀ��\�b�h�ɐ؂�o���܂��BLog�ɂł���Ȃ炻��ł��悵�B
     * @param message
     * @throws IOException
     */
    public void mqOutLog(String message) throws IOException {
        String outFilePath = (new File(eviOutputPath)).getPath() + File.separator + mqOutfilePrefix + fileNo + MQ_OUT_FILE_SYFIX;
        FileWriter fw = new FileWriter(outFilePath , (messageSendCallCount!=0));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(message);
        bw.newLine();
        bw.close();
        fw.close();
    }


    public void initOutMessage(String message) throws IOException {
        super.initOutMessage(message);
        messageSendCallCount = 0;
    }

    /**
     * mqOutfilePrefix��߂��B
     * <br>
     * @return  mqOutfilePrefix
     */
    public String getMqOutfilePrefix() {
        return mqOutfilePrefix;
    }

    /**
     * mqOutfilePrefix��ݒ肷��B
     * <br>
     * @param mqOutfilePrefix String
     */
    public void setMqOutfilePrefix(String mqOutfilePrefix) {
        this.mqOutfilePrefix = mqOutfilePrefix;
    }


    /**
     * excludeFields��߂��B
     * <br>
     * @return  excludeFields
     */
    public List<String> getExcludeFields() {
        return excludeFields;
    }


    /**
     * excludeFields��ݒ肷��B
     * <br>
     * @param excludeFields List<String>
     */
    public void setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

}
