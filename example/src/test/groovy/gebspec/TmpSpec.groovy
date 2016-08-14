package gebspec

import geb.spock.GebSpec

class TmpSpec extends GebSpec {

    def 'tmp case'() {
        when:
        go "/index.html"

        then:
        title == "test page"
        $('p', id: 'greeting').text() == "Hello World"
    }

}
