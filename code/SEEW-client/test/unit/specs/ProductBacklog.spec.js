import Vue from 'vue'
import { mount } from '@vue/test-utils'
import ProductBacklog from '@/components/ProductBacklog.vue'

function getEpics (nodes) {
  const values = []
  for (let index = 0; index < nodes.length; index++) {
    values[index] = nodes[index].querySelector('span').innerText
  }
}

describe('ProductBacklog.vue', () => {
  it('swaps epics according to input in first element', () => {
    const wrapper = mount(ProductBacklog, {
      attachToDocument: true
    })
    const vm = wrapper.vm

    // Alle vorherigen Werte
    const prevEpicsValues = getEpics(vm.$el.querySelectorAll('.allEpics'))
    console.log(prevEpicsValues)

    const firstInput = vm.$el.querySelector('.allEpics input')
    firstInput.value = 3

    // Gibt 2 im ersten Input ein und löst das keypress-Event aus
    const wrapperInput = wrapper.find('input')
    wrapperInput.setValue('2')
    wrapperInput.trigger('keypress.enter')

    // Alle nachfolgenden Werte
    const afterEpicsValues = getEpics(vm.$el.querySelectorAll('.allEpics'))
    // Vergleiche vorher mit nachher
    assert.deepEqual(prevEpicsValues, afterEpicsValues)
  })
})
