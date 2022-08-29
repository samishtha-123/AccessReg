import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.exception.StepErrorException as StepErrorException
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.relevantcodes.extentreports.LogStatus as LogStatus

import internal.GlobalVariable as GlobalVariable
//====================================================================================
String ReportFile = GlobalVariable.G_ReportName + '.html'
def extent = CustomKeywords.'generateReports.GenerateReport.create'(ReportFile, GlobalVariable.G_Browser, GlobalVariable.G_BrowserVersion)
def LogStatus = com.relevantcodes.extentreports.LogStatus
def extentTest = extent.startTest(TestCaseName)

TestObject newFileObj

WebUI.setText(findTestObject('LoginPage/username_txtbx'), '204044247')
WebUI.setText(findTestObject('LoginPage/password_txtbx'), '204044247')
WebUI.click(findTestObject('LoginPage/login_btn'))
extentTest.log(LogStatus.PASS, 'Entered Creds - username - '+GlobalVariable.G_userName +' password - '+GlobalVariable.G_Password)

extentTest.log(LogStatus.PASS, 'Clicked on Login Button ')
//=====================================================================================

println "*****************************************************"
println GlobalVariable.G_Platform
println "*****************************************************"

def navLocation = CustomKeywords.'generateFilePath.filePath.execLocation'()
def location = (navLocation + '/JobsModule/') 
println "*****************************************************"
println location
println "*****************************************************"




try {
	def jobsTab = CustomKeywords.'customWait.WaitForElement.WaitForelementPresent'(findTestObject('GenericObjects/JobsTab_disabled'),
			20, extentTest, 'Jobs Tab')

	if (jobsTab) {
		WebUI.click(findTestObject('GenericObjects/TitleLink_Jobs'))
	}
	extentTest.log(LogStatus.PASS, 'Navigated Jobs Tab')
	WebUI.delay(2)

	TestObject newAppObj = WebUI.modifyObjectProperty(findTestObject('NewJobPage/AppList_ShellScript'), 'id', 'equals',
			AppName, true)
	WebUI.click(newAppObj)
	extentTest.log(LogStatus.PASS, 'Navigated to Job Submission For for - ' + AppName)
	WebUI.delay(2)

	def errorPanel = CustomKeywords.'customWait.WaitForElement.WaitForelementPresent'(findTestObject('JobSubmissionForm/JS_ErrorModalPanel'),
			3, extentTest, 'Error Panel Close Icon')
	if (errorPanel) {
		WebUI.click(findTestObject('Object Repository/JobSubmissionForm/button_Close'))
	}
	WebUI.click(findTestObject('Object Repository/NewJobPage/GenericProfile'))
	WebUI.delay(2)
	
	if(AppName.contains('InComplete'))
	{
		WebUI.click(findTestObject('Object Repository/JobSubmissionForm/TxtBox_ReqFiled_ToFill'))
		WebUI.setText(findTestObject('Object Repository/JobSubmissionForm/TxtBox_ReqFiled_ToFill'), 'testString')
	}

	CustomKeywords.'operations_JobsModule.JobSubmissions.JSAllFileds'(ToChange, ChangeValue, extentTest)

	if (ExecMode.equals('Array'))
	{
		extentTest.log(LogStatus.PASS, 'No file required for Array Job')
	}
	else
	{
		WebUI.delay(2)
		WebUI.scrollToElement(findTestObject('JobSubmissionForm/Link_Server'), 3)
		WebUI.delay(3)

		newFileObj = CustomKeywords.'operations_JobsModule.JobSubmissions.selectFile'(ExecMode, InputFile, extentTest)
		WebUI.click(newFileObj)
		WebUI.rightClick(newFileObj)
		extentTest.log(LogStatus.PASS, 'Right Clicked on Input file ' + InputFile)
		WebUI.delay(2)
		String idForCntxtMn = 'Add as ' + FileArg
		TestObject newRFBContextMnOption = WebUI.modifyObjectProperty(findTestObject('Object Repository/NewJobPage/ContextMenu_RFB_FilePicker'),
				'id', 'equals', idForCntxtMn, true)
		WebUI.click(newRFBContextMnOption)
		extentTest.log(LogStatus.PASS, 'Clicked on context menu - ' + idForCntxtMn)
	}

	def submitBtn = CustomKeywords.'customWait.WaitForElement.WaitForelementPresent'(findTestObject('JobSubmissionForm/button_Submit_Job'),
			20, extentTest, 'Submit Button')
	if (submitBtn) {
		WebUI.click(findTestObject('JobSubmissionForm/button_Submit_Job'))
		extentTest.log(LogStatus.PASS, 'Clicked on Submit Button ')
	}

	WebUI.waitForElementPresent(findTestObject('Notificactions/Notification_JobSubmission'), 5)
	def jobText = WebUI.getText(findTestObject('Notificactions/Notification_JobSubmission'))
	extentTest.log(LogStatus.PASS, 'Notification Generated')
	GlobalVariable.G_JobID=CustomKeywords.'operations_JobsModule.GetJobRowDetails.getJobID'(jobText)
	extentTest.log(LogStatus.PASS, 'Job ID - ' + GlobalVariable.G_JobID)


	if (ToChange.equals('SetOutPutDir')) {
		WebUI.click(findTestObject('GenericObjects/TitleLink_Files'))
		extentTest.log(LogStatus.PASS, 'Navigated to Files Tab')
		WebUI.delay(2)
		CustomKeywords.'operations_FileModule.ChangeView.changePageView'(TestCaseName, extentTest)
		WebUI.delay(2)
		WebUI.click(findTestObject('Object Repository/FilesPage/Icon_EditFilePath'))
		WebUI.setText(findTestObject('Object Repository/FilesPage/textBx_FilePath'), location)
		WebUI.sendKeys(findTestObject('Object Repository/FilesPage/textBx_FilePath'), Keys.chord(Keys.ENTER))
		extentTest.log(LogStatus.PASS, ('Navigated to ' + location) + ' in files tab')
		TestObject newFileObjJS = WebUI.modifyObjectProperty(findTestObject('FilesPage/RowItem_File_ListView'), 'title',
				'equals', InputFile, true)
		def fileItem = CustomKeywords.'customWait.WaitForElement.WaitForelementPresent'(newFileObjJS, 20, extentTest, InputFile)
		println(fileItem)
		if (fileItem) {
			extentTest.log(LogStatus.PASS, 'Output file - jobFile.out exists ')
		}
	}

	TestObject jobIdEle = CustomKeywords.'buildTestObj.CreateTestObjJobs.myTestObjJobRow'(GlobalVariable.G_JobID)
	WebUI.click(findTestObject('GenericObjects/TitleLink_Jobs'))
	WebUI.delay(2)

	if (ExecMode.equals('Array')) {
		WebUI.delay(2)
		CustomKeywords.'operations_JobsModule.GetJobRowDetails.checkSubJobs'(katalonWebDriver, 'JS', extentTest)
	}

	extentTest.log(LogStatus.PASS, 'Verified - ' + TestCaseName)

	if (GlobalVariable.G_Browser == 'Edge') {
		WebUI.callTestCase(findTestCase('Generic/Logout'), [:], FailureHandling.STOP_ON_FAILURE)
	}
}
catch (Exception ex) {
	String screenShotPath = (('ExtentReports/' + TestCaseName) + GlobalVariable.G_Browser) + '.png'
	WebUI.takeScreenshot(screenShotPath)
	String p = (TestCaseName + GlobalVariable.G_Browser) + '.png'
	extentTest.log(LogStatus.FAIL, ex)
	extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(p))
}
catch (StepErrorException e) {
	String screenShotPath = (('ExtentReports/' + TestCaseName) + GlobalVariable.G_Browser) + '.png'
	WebUI.takeScreenshot(screenShotPath)
	String p = (TestCaseName + GlobalVariable.G_Browser) + '.png'
	extentTest.log(LogStatus.FAIL, ex)
	extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(p))
}
finally {
	extent.endTest(extentTest)
	extent.flush()
}



