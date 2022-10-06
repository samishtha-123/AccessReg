import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable


def Browser = GlobalVariable.G_Browser
//====================================================================================
ReportFile = (GlobalVariable.G_ReportName + '.html')
def extent = CustomKeywords.'generateReports.GenerateReport.create'(ReportFile, GlobalVariable.G_Browser, GlobalVariable.G_BrowserVersion)
def LogStatus = com.relevantcodes.extentreports.LogStatus
def extentTest = extent.startTest(TestCaseName)
//=====================================================================================


extentTest.log(LogStatus.PASS, 'Navigated to Acces Instance - '+GlobalVariable.G_BaseUrl)

WebUI.setText(findTestObject('LoginPage/username_txtbx'), GlobalVariable.G_userName)

WebUI.setText(findTestObject('LoginPage/password_txtbx'), GlobalVariable.G_Password)


extentTest.log(LogStatus.PASS, 'Entered Creds - username - '+GlobalVariable.G_userName +' password - '+GlobalVariable.G_Password)

WebUI.click(findTestObject('LoginPage/login_btn'))
extentTest.log(LogStatus.PASS, 'Clicked on Login Button ')


def jobsTab = CustomKeywords.'customWait.WaitForElement.WaitForelementPresent'(findTestObject('Object Repository/Landing_Page/LandigPage_AltairAccess_Link'),
		10,extentTest, 'AltairAccess Logo ')

if (jobsTab) {
	WebUI.click(findTestObject('Object Repository/Landing_Page/LandigPage_AltairAccess_Link'))
}

extentTest.log(LogStatus.PASS, 'Verified AltairAccess Logo post login ')
