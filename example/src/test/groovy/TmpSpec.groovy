import geb.spock.GebSpec

class TmpSpec extends GebSpec {

    def indexHtml = "file://${System.getProperty('projectDir')}/site/index.html"

    void 'tmp case'() {
        when:
        go indexHtml

        then:
        title == "test page"
        $('p', id: 'greeting').text() == "Hello World"
    }

}
